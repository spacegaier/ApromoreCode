package org.apromore.service.impl;

import nl.tue.tm.is.graph.SimpleGraph;
import org.apromore.clustering.dissimilarity.measure.GEDDissimCalc;
import org.apromore.dao.ClusterAssignmentRepository;
import org.apromore.dao.ClusterRepository;
import org.apromore.dao.FragmentVersionRepository;
import org.apromore.dao.model.Cluster;
import org.apromore.dao.model.ClusteringSummary;
import org.apromore.dao.model.FragmentVersion;
import org.apromore.dao.model.ProcessModelVersion;
import org.apromore.exception.LockFailedException;
import org.apromore.exception.RepositoryException;
import org.apromore.graph.canonical.Canonical;
import org.apromore.service.ClusterService;
import org.apromore.service.FragmentService;
import org.apromore.service.helper.SimpleGraphWrapper;
import org.apromore.service.model.ClusterFilter;
import org.apromore.service.model.ClusterSettings;
import org.apromore.service.model.MemberFragment;
import org.apromore.service.model.ProcessAssociation;
import org.apromore.toolbox.clustering.algorithms.dbscan.FragmentPair;
import org.apromore.toolbox.clustering.algorithms.dbscan.InMemoryClusterer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

/**
 * Implementation of the ClusterService Contract.
 *
 * @author <a href="mailto:cam.james@gmail.com">Cameron James</a>
 */
@Service
@Transactional
public class ClusterServiceImpl implements ClusterService {

    private ClusterRepository cRepository;
    private ClusterAssignmentRepository caRepository;
    private FragmentVersionRepository fvRepository;
    private FragmentService fService;
    private DistanceMatrix matrix;
    private InMemoryClusterer clusterer;


    /**
     * Default Constructor allowing Spring to Autowire for testing and normal use.
     * @param clusterRepository Cluster Repository.
     * @param clusterAssignmentRepository Cluster Assignment Repository.
     * @param fragmentVersionRepository Fragment Version Repository.
     * @param fragmentService Fragment Repository.
     * @param distanceMatrix Distance Matrix.
     * @param inMemoryClusterer in Memory Clusterer.
     */
    @Inject
    public ClusterServiceImpl(final ClusterRepository clusterRepository, final ClusterAssignmentRepository clusterAssignmentRepository,
                              final FragmentVersionRepository fragmentVersionRepository, final FragmentService fragmentService,
                              final DistanceMatrix distanceMatrix, final InMemoryClusterer inMemoryClusterer) {
        cRepository = clusterRepository;
        caRepository = clusterAssignmentRepository;
        fvRepository = fragmentVersionRepository;
        fService = fragmentService;
        matrix = distanceMatrix;
        clusterer = inMemoryClusterer;
    }


    /**
     * @see org.apromore.service.ClusterService#assignFragments(java.util.List, Integer)
     * {@inheritDoc}
     */
    @Override
    public void assignFragments(List<Integer> fragmentIds, Integer clusterId) {
        for (Integer frag : fragmentIds) {
            assignFragment(frag, clusterId);
        }
    }

    /**
     * @see org.apromore.service.ClusterService#assignFragment(Integer, Integer)
     * {@inheritDoc}
     */
    @Override
    public void assignFragment(Integer fragmentId, Integer clusterId) {
        FragmentVersion fragVersion = fvRepository.findOne(fragmentId);
        fragVersion.setCluster(cRepository.findOne(clusterId));
        fvRepository.save(fragVersion);
    }

    /**
     * @see org.apromore.service.ClusterService#cluster(org.apromore.service.model.ClusterSettings)
     * {@inheritDoc}
     */
    @Override
    public void cluster(ClusterSettings settings) throws RepositoryException {
        computeDistanceMatrix();
        clearClusters();
        clusterer.clusterRepository(settings);
    }

    /**
     * @see org.apromore.service.ClusterService#getClusteringSummary
     * {@inheritDoc}
     */
    @Override
    public ClusteringSummary getClusteringSummary() {
        ClusteringSummary summary = new ClusteringSummary();
        List<Object[]> summaryObj = cRepository.getClusteringSummary();
        for (Object[] objects : summaryObj) {
            summary.setNumClusters((Integer) objects[0]);
            summary.setMinClusterSize((Integer) objects[1]);
            summary.setMaxClusterSize((Integer) objects[2]);
            summary.setMinAvgFragmentSize((Integer) objects[3]);
            summary.setMaxAvgFragmentSize((Integer) objects[4]);
            summary.setMinBCR((Integer) objects[5]);
            summary.setMaxBCR((Integer) objects[6]);
        }
        return summary;
    }

    /**
     * @see org.apromore.service.ClusterService#getClusterSummaries(org.apromore.service.model.ClusterFilter)
     * {@inheritDoc}
     */
    @Override
    public List<Cluster> getClusterSummaries(ClusterFilter filter) {
        return cRepository.getFilteredClusters(filter);
    }

    /**
     * @see org.apromore.service.ClusterService#getCluster(Integer)
     * {@inheritDoc}
     */
    @Override
    public org.apromore.service.model.Cluster getCluster(Integer clusterId) {
        Cluster cinfo = cRepository.findOne(clusterId);

        org.apromore.service.model.Cluster c = new org.apromore.service.model.Cluster();
        c.setCluster(cinfo);
        List<FragmentVersion> fs = fvRepository.getFragmentsByClusterId(clusterId);
        for (FragmentVersion f : fs) {
            MemberFragment fragment = new MemberFragment(f.getId());
            fragment.setFragmentSize(f.getFragmentSize());
            Set<ProcessModelVersion> pmap = f.getProcessModelVersions();
            for (ProcessModelVersion m : pmap) {
                Integer pmvid = m.getId();
                Double pmvNumber = m.getVersionNumber();
                String branchName = m.getProcessBranch().getBranchName();
                Integer processId = m.getProcessBranch().getProcess().getId();
                String processName = m.getProcessBranch().getProcess().getName();

                ProcessAssociation pa = new ProcessAssociation();
                pa.setProcessVersionId(pmvid);
                pa.setProcessVersionNumber(pmvNumber);
                pa.setProcessBranchName(branchName);
                pa.setProcessId(processId);
                pa.setProcessName(processName);
                fragment.getProcessAssociations().add(pa);
            }
            double distance = cRepository.getDistance(cinfo.getMedoidId(), f.getId());
            fragment.setDistance(distance);
            c.addFragment(fragment);
        }
        return c;
    }

    /**
     * @see org.apromore.service.ClusterService#getClusters()
     * {@inheritDoc}
     */
    @Override
    public List<Cluster> getClusters() {
        return cRepository.findAll();
    }

    /**
     * @see org.apromore.service.ClusterService#getClusters(org.apromore.service.model.ClusterFilter)
     * {@inheritDoc}
     */
    @Override
    public List<org.apromore.service.model.Cluster> getClusters(ClusterFilter filter) {
        List<org.apromore.service.model.Cluster> clusters = new ArrayList<org.apromore.service.model.Cluster>();
        List<Cluster> cinfos = cRepository.getFilteredClusters(filter);
        for (Cluster cinfo : cinfos) {
            org.apromore.service.model.Cluster c = new org.apromore.service.model.Cluster();
            c.setCluster(cinfo);
            List<FragmentVersion> fs = fvRepository.getFragmentsByClusterId(cinfo.getId());
            for (FragmentVersion f : fs) {
                MemberFragment fragment = new MemberFragment(f.getId());
                fragment.setFragmentSize(f.getFragmentSize());
                Set<ProcessModelVersion> pmap = f.getProcessModelVersions();
                for (ProcessModelVersion m : pmap) {
                    Integer pmvid = m.getId();
                    Double pmvNumber = m.getVersionNumber();
                    String branchName = m.getProcessBranch().getBranchName();
                    Integer processId = m.getProcessBranch().getProcess().getId();
                    String processName = m.getProcessBranch().getProcess().getName();

                    ProcessAssociation pa = new ProcessAssociation();
                    pa.setProcessVersionId(pmvid);
                    pa.setProcessVersionNumber(pmvNumber);
                    pa.setProcessBranchName(branchName);
                    pa.setProcessId(processId);
                    pa.setProcessName(processName);
                    fragment.getProcessAssociations().add(pa);
                }
                double distance = cRepository.getDistance(cinfo.getMedoidId(), f.getId());
                fragment.setDistance(distance);
                c.addFragment(fragment);
            }
            clusters.add(c);
        }
        return clusters;
    }

    /**
     * @see org.apromore.service.ClusterService#getFragmentIds(Integer)
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getFragmentIds(Integer clusterId) {
        return cRepository.getFragmentIds(clusterId);
    }

    /**
     * @see org.apromore.service.ClusterService#getPairDistances(java.util.List)
     * {@inheritDoc}
     */
    @Override
    public Map<FragmentPair, Double> getPairDistances(List<Integer> fragmentIds) throws RepositoryException {
        Map<FragmentPair, Double> pairDistances = new HashMap<FragmentPair, Double>(0);

        for (int i = 0; i < fragmentIds.size() - 1; i++) {
            for (int j = i + 1; j < fragmentIds.size(); j++) {
                Integer fid1 = fragmentIds.get(i);
                Integer fid2 = fragmentIds.get(j);
                double distance = cRepository.getDistance(fid1, fid2);
                if (distance < 0) {

                    try {
                        Canonical g1 = fService.getFragment(fid1, false);
                        Canonical g2 = fService.getFragment(fid2, false);

                        SimpleGraph sg1 = new SimpleGraphWrapper(g1);
                        SimpleGraph sg2 = new SimpleGraphWrapper(g2);

                        GEDDissimCalc calc = new GEDDissimCalc(1, 0.4);
                        distance = calc.compute(sg1, sg2);

                    } catch (LockFailedException e) {
                        throw new RepositoryException(e);
                    }
                }

                FragmentPair pair = new FragmentPair(fvRepository.findOne(fid1), fvRepository.findOne(fid2));
                pairDistances.put(pair, distance);
            }
        }

        return pairDistances;
    }


    /**
     * @see org.apromore.service.ClusterService#clearClusters()
     * {@inheritDoc}
     */
    @Override
    public void clearClusters() {
        cRepository.deleteAll();
        caRepository.deleteAll();
    }



    /* Computers the fragment distances. */
    private void computeDistanceMatrix() {
        try {
            matrix.compute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
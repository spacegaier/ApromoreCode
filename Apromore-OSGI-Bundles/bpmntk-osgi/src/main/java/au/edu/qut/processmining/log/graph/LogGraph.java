package au.edu.qut.processmining.log.graph;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.analysis.petrinet.cpnexport.IntegerColorSet;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramImpl;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Adriano on 14/06/2016.
 */
public class LogGraph {
    private HashSet<LogEdge> edges;
    private HashMap<String, LogNode> nodes;

    private HashMap<String, LogEdge> outgoing;
    private HashMap<String, LogEdge> incoming;

    private HashMap<String, HashMap<String, LogEdge>> graph;
    private HashMap<String, HashSet<String>> eventuallyFollow;

    private HashSet<String> startEvents;
    private HashSet<String> endEvents;

    private HashMap<String, Integer> minTime;
    private HashMap<String, Integer> maxTime;

    private int totalTraces;

    public LogGraph(XLog log) {
        nodes = new HashMap<>();
        edges = new HashSet<>();
        outgoing = new HashMap<>();
        incoming = new HashMap<>();
        startEvents = new HashSet<>();
        endEvents = new HashSet<>();
        graph = new HashMap<>();
        eventuallyFollow = new HashMap<>();
        totalTraces = log.size();
        minTime = new HashMap<>();
        maxTime = new HashMap<>();

        generateGraph(log);
    }

    private void generateGraph(XLog log) {
        HashSet<String> executed;
        int tIndex;
        int eIndex;

        XTrace trace;
        XEvent event;

        String name;
        String prevName;

        LogNode node;
        LogNode prevNode;
        LogEdge edge;

        int totalEvents;
        System.out.println("DEBUG - total traces: " + totalTraces);

        for( tIndex = 0; tIndex < totalTraces; tIndex++ ) {
            trace = log.get(tIndex);
            totalEvents = trace.size();
            System.out.println("DEBUG - analyzing trace: " + tIndex);
            prevName = null;
            prevNode = null;
            node = null;

            executed = new HashSet<>();

            for( eIndex = 0; eIndex < totalEvents; eIndex++ ) {
                event = trace.get(eIndex);
                name = event.getAttributes().get("concept:name").toString();

                if( !minTime.containsKey(name) ) {
                    minTime.put(name, eIndex+1);
                    maxTime.put(name, eIndex+1);
                } else {
                    if( minTime.get(name) > (eIndex+1) ) minTime.put(name, eIndex+1);
                    if( maxTime.get(name) < (eIndex+1) ) maxTime.put(name, eIndex+1);
                }


                for( String e : executed ) {
                    if( !eventuallyFollow.containsKey(e) ) eventuallyFollow.put(e, new HashSet<String>());
                    eventuallyFollow.get(e).add(name);
                }
                if( !executed.contains(name) ) executed.add(name);

                if( !nodes.containsKey(name) ) {
                    node =  new LogNode(name);
                    nodes.put(name, node);
                } else node = nodes.get(name);

                node.increaseWeight();
                if( prevName == null ) {
                    startEvents.add(name);
                    node.incStartingTimes();
                }

                if( !graph.containsKey(prevName) ) graph.put(prevName, new HashMap<String, LogEdge>());
                if( !graph.get(prevName).containsKey(name) ) {
                    edge = new LogEdge(prevNode, node);
                    edges.add(edge);
                    incoming.put(name, edge);
                    outgoing.put(prevName, edge);
                    graph.get(prevName).put(name, edge);
                }
                graph.get(prevName).get(name).increaseWeight();

                prevName = name;
                prevNode = node;
            }

            name = null;
            node = null;

            if( !graph.containsKey(prevName) ) graph.put(prevName, new HashMap<String, LogEdge>());
            if( !graph.get(prevName).containsKey(name) ) {
                edge = new LogEdge(prevNode, node);
                edges.add(edge);
                incoming.put(name, edge);
                outgoing.put(prevName, edge);
                graph.get(prevName).put(name, edge);
            }
            graph.get(prevName).get(name).increaseWeight();
            endEvents.add(prevName);
        }
    }


    public BPMNDiagram getLogAsDiagram() {
        BPMNDiagram diagram = new BPMNDiagramImpl("Log-diagram");
        HashMap<String, BPMNNode> mapping = new HashMap<>();
        Activity task;
        Event event;
        LogNode src, tgt;
        String label;
        BPMNNode srcNode, tgtNode;

        event = diagram.addEvent("start-autogen\n("+ totalTraces +")", Event.EventType.START, Event.EventTrigger.NONE, Event.EventUse.CATCH, false, null);
        mapping.put("start-autogen", event);

        event = diagram.addEvent("end-autogen\n("+ totalTraces +")", Event.EventType.END, Event.EventTrigger.NONE, Event.EventUse.THROW, false, null);
        mapping.put("end-autogen", event);

        for( String name : nodes.keySet() ) {
            label = name + "\n(" + nodes.get(name).getWeight() + ")";
            task = diagram.addActivity(label, false, false, false, false, false);
            mapping.put(name, task);
        }

        for( LogEdge edge : edges ) {
            src = edge.getSource();
            tgt = edge.getTarget();

            if( src == null ) srcNode = mapping.get("start-autogen");
            else srcNode = mapping.get(src.getName());

            if( tgt == null ) tgtNode = mapping.get("end-autogen");
            else tgtNode = mapping.get(tgt.getName());

            diagram.addFlow(srcNode, tgtNode, Integer.toString(edge.getWeight()));
        }

        return diagram;
    }


    public boolean isStart(String name) { return startEvents.contains(name); }
    public boolean isEnd(String name) { return endEvents.contains(name); }

    public boolean isDirectlyFollow(String follower, String node) {
        return (graph.containsKey(node) && graph.get(node).containsKey(follower));
    }

    public boolean isEventuallyFollow(String follower, String node) {
        return (eventuallyFollow.containsKey(node) && eventuallyFollow.get(node).contains(follower));
    }

    public BPMNDiagram getEventuallyFollowGraph() {
        BPMNDiagram diagram = new BPMNDiagramImpl("Eventually-Follow-Diagram");
        HashMap<String, HashSet<String>> edges = new HashMap<>();
        HashMap<String, BPMNNode> mapping = new HashMap<>();
        BPMNNode src, tgt;

        for( String node : eventuallyFollow.keySet() ) {
            if( !mapping.containsKey(node) ) {
                src = diagram.addActivity(node, false, false, false, false, false);
                mapping.put(node, src);
            } else src = mapping.get(node);

            for( String follower : eventuallyFollow.get(node) ) {
                if( !mapping.containsKey(follower) ) {
                    tgt = diagram.addActivity(follower, false, false, false, false, false);
                    mapping.put(follower, tgt);
                } else tgt = mapping.get(follower);

                if( !edges.containsKey(node) ) edges.put(node, new HashSet<String>());
                if( !edges.get(node).contains(follower) ) {
                    diagram.addFlow(src, tgt, "");
                    edges.get(node).add(follower);
                }
            }
        }

        return diagram;
    }

    public int getWeight(String name){ return nodes.get(name).getWeight(); }
    public int getMinTime(String name){ return minTime.get(name); }
    public int getMaxTime(String name){ return maxTime.get(name); }

}

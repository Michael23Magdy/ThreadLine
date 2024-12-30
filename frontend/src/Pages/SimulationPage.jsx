import React, { useCallback, useMemo, useRef } from 'react';
import {
  ReactFlow,
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from '@xyflow/react';
 
import '@xyflow/react/dist/style.css';
import ToolBar from '../components/ToolBar';
import Machine from '../components/CustomNodes/Machine';
import Queue from '../components/CustomNodes/Queue';
import ProductionLine from '../components/CustomEdges/ProductionLine';

const initialNodes = [
    {
        id: 'Input',
        type: 'queue',
        deletable: false,
        position: { x: -300, y: 0 },
        data: { count: 0, id: 'Input' },
    },
    {
        id: 'Output',
        type: 'queue',
        deletable: false,
        position: { x: 300, y: 0 },
        data: { count: 0, id: 'Output' },
    }
];
const initialEdges = [];
const edgeTypes = {'production-line': ProductionLine}
const nodeTypes = { machine: Machine, queue: Queue }

const SimulationPage = ()=>{
    const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
    const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);
    const machineCounter = useRef(0);
    const queueCounter = useRef(0);

    const addMachine = ()=>{
        machineCounter.current++
        const new_id = `M${machineCounter.current}`
        setNodes([...nodes, {id: new_id, type: 'machine', position: {x:0, y:0}, data:{active:false, id: new_id}}])
    }
    const addQueue = ()=>{
        queueCounter.current++
        const new_id = `Q${queueCounter.current}`
        setNodes([...nodes, {id: new_id, type: 'queue', position: {x:0, y:0}, data:{count:0, id: new_id}}])
    }
    const onClear = ()=>{
        setNodes(initialNodes);
        setEdges(initialEdges);
        machineCounter.current=0;
        queueCounter.current=0;
    }
    

    const onConnect = useCallback((params) => {
        const sourceNode = nodes.find(node => node.id === params.source);
        const targetNode = nodes.find(node => node.id === params.target);
        if (sourceNode && targetNode && sourceNode.type !== targetNode.type) {
            setEdges(eds => addEdge({ ...params, type: 'production-line', animated: true }, eds));
        } else {
            alert("can't connect "+sourceNode.type+" to a "+ targetNode.type);  
        }
    }, [nodes, edges, setEdges]);
    return(
        <>
            {console.log(nodes)}
            {console.log(edges)}
            <ToolBar 
                addMachine={addMachine}
                addQueue={addQueue}
                onClear={onClear}    
            />
            <div className={`w-full h-lvh bg-slate-100`}>
                <ReactFlow
                    nodeTypes={nodeTypes}
                    edgeTypes={edgeTypes}
                    nodes={nodes}
                    edges={edges}
                    onNodesChange={onNodesChange}
                    onEdgesChange={onEdgesChange}
                    onConnect={onConnect}
                    fitView
                >
                    <Background />
                    <Controls />
                </ReactFlow>
            </div>
        </>
    )
}
export default SimulationPage
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
import { IDs } from '../constants/IDs';
import { Types } from '../constants/Types';

const initialNodes = [
    {
        id: IDs.input,
        type: Types.queue,
        deletable: false,
        position: { x: -300, y: 0 },
        data: { count: 0, id: 'Input' },
    },
    {
        id: IDs.output,
        type: Types.queue,
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
        setNodes([...nodes, {id: new_id, type: Types.machine, position: {x:0, y:0}, data:{active:false, id: new_id}}])
    }

    const editNode = (newData)=>{
        setNodes((p)=>
            p.map((node)=> node.id === newData.id ? {...node, data: newData} : node)
        )
    }

    const addQueue = ()=>{
        queueCounter.current++
        const new_id = `Q${queueCounter.current}`
        setNodes([...nodes, {id: new_id, type: Types.queue, position: {x:0, y:0}, data:{count:0, id: new_id}}])
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
            if(sourceNode.type == Types.machine && edges.find(edge => edge.source == sourceNode.id)){
                alert("machines can output to one queue")
            } else {
                setEdges(eds => addEdge({ ...params, type: 'production-line', animated: true }, eds));
            }
        } else {
            alert("can't connect "+sourceNode.type+" to a "+ targetNode.type);  
        }
    }, [nodes, edges, setEdges]);
    return(
        <>
            {console.log(nodes)}
            {console.log(edges)}
            {/* <button onClick={()=>editNode({id:"Input", count:2})}>ssssssssss</button> */}
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
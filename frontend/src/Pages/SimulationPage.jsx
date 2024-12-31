import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
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
import { useWebSocket } from '../hooks/useWebSocket';
import useSendControls from '../hooks/useSendControls';

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
    const [running, setRunning] = useState(false);
    const machineCounter = useRef(0);
    const queueCounter = useRef(0);
    const ws = useWebSocket();
    const api = useSendControls();
    
    useEffect(() => {
        ws.setNodeEditHandler(editNode);
    }, []);

    const addMachine = ()=>{
        machineCounter.current++;
        const newMachine = {
            id: `M${machineCounter.current}`,
            type: Types.machine,
            position: { x: 0, y: 0 },
            data: { active: false, id: `M${machineCounter.current}` }
        };
        setNodes(nodes => [...nodes, newMachine]);
    }

    const addQueue = ()=>{
        queueCounter.current++;
        const newQueue = {
            id: `Q${queueCounter.current}`,
            type: Types.queue,
            position: { x: 0, y: 0 },
            data: { count: 0, id: `Q${queueCounter.current}` }
        };
        setNodes(nodes => [...nodes, newQueue]);
    }
    
    const editNode = (newData)=>{
        setNodes(nodes=>
            nodes.map(node => node.id === newData.id ? {...node, data: newData} : node)
        )
    }
    
    const startSimulation = (numProducts) => api.sendStartSimulation({ nodes, edges, numProducts });
    const reSimulate = (numProducts) => api.sendReSimulate(numProducts);
    const stopSimulation = () => api.sendStopSimulation();
    const onClear = () => {
        setNodes(initialNodes);
        setEdges([]);
        machineCounter.current = 0;
        queueCounter.current = 0;
        api.sendClear();
    };
    

    const onConnect = useCallback((params) => {
        const sourceNode = nodes.find(node => node.id === params.source);
        const targetNode = nodes.find(node => node.id === params.target);

        if (sourceNode && targetNode && sourceNode.type !== targetNode.type) {
            if(sourceNode.type == Types.machine && edges.some(edge => edge.source == sourceNode.id)){
                alert("machines can output to one queue");
                return;
            }
            const newEdge = { ...params, type: 'production-line', animated: true };
            setEdges(eds => addEdge(newEdge, eds));            
        } else {
            alert("can't connect "+sourceNode.type+" to a "+ targetNode.type);  
        }
    }, [nodes, edges, setEdges]);

    return(
        <>
            <ToolBar 
                addMachine={addMachine}
                addQueue={addQueue}
                onClear={onClear}
                startSimulation={startSimulation}
                reSimulate={reSimulate}
                stopSimulation={stopSimulation}  
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
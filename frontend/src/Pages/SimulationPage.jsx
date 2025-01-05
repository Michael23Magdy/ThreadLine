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
import { SimulationStates } from '../constants/States';
import checkValidConnection from '../utils/checkValidConnection';

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
    const [simulationState, setSimulationState] = useState(SimulationStates.compose);
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
        if(newData.type == Types.machine)
            console.log("------"+newData.type+": "+newData.id+", active: "+newData.active);
        else 
            console.log(newData.type+": "+newData.id+", count: "+newData.count);
        setNodes(nodes=>
            nodes.map(node => node.id === newData.id ? {...node, data: newData} : node)
        )
    }
    const resetNodes = ()=>{
        const resetedNodes = nodes.map((node)=>{
            if(node.type == Types.machine) {
                node.data.active = false;
                node.data.color = null;
            }
            else node.data.count = 0;
            return node
        });
        setNodes(resetedNodes);
    }
    
    const startSimulation = (numProducts) => {
        if(checkValidConnection(nodes, edges)){
            setSimulationState(SimulationStates.running)
            api.sendStartSimulation({ nodes, edges, numProducts });
        } else {
            alert("invalid connections");
        }
    }
    const replaySimulation = (numProducts) => {
        setSimulationState(SimulationStates.running);
        api.sendReplaySimulation(numProducts);
        resetNodes();
    }
    const pauseSimulation = () => {
        setSimulationState(SimulationStates.paused)
        api.sendPauseSimulation()
    };
    const reusmeSimulation = () => {
        setSimulationState(SimulationStates.running)
        api.sendResumeSimulation();
    }
    const clearSimulation = () => {
        setSimulationState(SimulationStates.clear);
        setNodes(initialNodes);
        setEdges([]);
        machineCounter.current = 0;
        queueCounter.current = 0;
    };
    const resetSimulation = () => {
        setSimulationState(SimulationStates.compose)
        api.sendClearSimulation();
        resetNodes()
    }

    

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

                simulationState={ simulationState }

                startSimulation={startSimulation}
                replaySimulation={replaySimulation}
                pauseSimulation={pauseSimulation}
                resumeSimulation={reusmeSimulation}
                clearSimulation={clearSimulation}
                resetSimulation={resetSimulation}
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
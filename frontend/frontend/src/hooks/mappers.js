import { Types } from "../constants/Types"

export const extractQueues = (nodes)=>{
    const queues = nodes.filter((node)=> node.type == Types.queue);
    const queuesIds = queues.map((node)=> node.id);
    return queuesIds
}
export const extractMachines = (nodes)=>{
    const machines = nodes.filter((node)=> node.type == Types.machine);
    const machinesIds = machines.map((machine)=> machine.id);
    return machinesIds
}
export const extractEdges = (edges)=>{
    const edgesDto = edges.map((edge) => 
        ({source: edge.source, target: edge.target}));
    return edgesDto
}
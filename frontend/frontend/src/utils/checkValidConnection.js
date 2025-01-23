import { IDs } from "../constants/IDs";

/**
 * Checks if all paths go from the input node to the output node in a directed graph.
 * 
 * @param {Array} nodes - Array of node objects, each with an `id` property.
 * @param {Array} edges - Array of edge objects, each with `source` and `target` properties representing node IDs.
 * @returns {boolean} - Returns true if all connections are valid from the input node to the output node and all nodes are visited, otherwise false.
 */

export default function checkValidConnection(nodes, edges){ 
   
    let graph = new Map();
    let visitedNodes = new Map();
    let inStack = new Map();
    nodes.forEach(node => {
        graph.set(node.id, []);      
        visitedNodes.set(node.id, false);  
        inStack.set(node.id, false);  
    });
    edges.forEach(edge => {
        graph.get(edge.source).push(edge.target);
    });

    const dfs = (nodeId) => {
        if(inStack.get(nodeId)) return false;
        if(visitedNodes.get(nodeId)) return true;
        inStack.set(nodeId, true);
        visitedNodes.set(nodeId, true);
        if(nodeId == IDs.output) {
            inStack.set(nodeId, false)
            return true;
        }

        const isChildrenValid = graph.get(nodeId).every(child => dfs(child));
        inStack.set(nodeId, false);

        return isChildrenValid
    }
    if (!graph.has(IDs.input)) return false;

    const isInputToOutputValid = dfs(IDs.input);
    const allNodesVisited = Array.from(visitedNodes.values()).every(n => n);

    return isInputToOutputValid && allNodesVisited;
}
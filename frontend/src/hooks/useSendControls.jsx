import axios from 'axios';
import { useCallback } from 'react';
import { extractEdges, extractMachines, extractQueues } from './mappers';

// Generic function to handle requests
const handleRequest = async (requestPromise) => {
    try {
        const response = await requestPromise;
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.log(error);
        return null;
    }
};

const useSendControls = () => {
  const api = axios.create({
    baseURL: "http://localhost:8080/api/simulation/",
    headers: {
      'Content-Type': 'application/json',
    },
  });

  const sendStartSimulation = useCallback(({ nodes, edges, numProducts}) => {
    const machines = extractMachines(nodes);
    const queues = extractQueues(nodes);
    const edgeDtos = extractEdges(edges);
    const request = {
        machines: machines,
        queues: queues,
        edges: edgeDtos,
        products: numProducts
    }
    console.log(request);
    return handleRequest(api.post('/start', request));
  }, [api]);

  const sendReSimulate = useCallback((Products) => {
    return handleRequest(api.post('/reSimulate', Products));
  }, [api]);

  const sendStopSimulation = useCallback(() => {
    return handleRequest(api.post('/stop'));
  }, [api]);

  const sendClear = useCallback(() => {
    return handleRequest(api.post('/clear'));
  }, [api]);

  // Return the functions so they can be used by components
  return {
    sendStartSimulation,
    sendReSimulate,
    sendStopSimulation,
    sendClear
  };
};

export default useSendControls;
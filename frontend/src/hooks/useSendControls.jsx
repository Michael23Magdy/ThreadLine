import axios from 'axios';
import { useCallback } from 'react';
import { extractEdges, extractMachines, extractQueues } from './mappers';

// Generic function to handle requests
const handleRequest = async (requestPromise) => {
    try {
        const response = await requestPromise;
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
    return handleRequest(api.post('/start', request));
  }, [api]);

  const sendReplaySimulation = useCallback((Products) => {
    return handleRequest(api.post('/replay', Products));
  }, [api]);

  const sendPauseSimulation = useCallback(() => {
    return handleRequest(api.post('/pause'));
  }, [api]);
  
  const sendResumeSimulation = useCallback(() => {
    return handleRequest(api.post('/resume'));
  }, [api]);

  const sendClearSimulation = useCallback(() => {
    return handleRequest(api.post('/clear'));
  }, [api]);


  // Return the functions so they can be used by components
  return {
    sendStartSimulation,
    sendReplaySimulation,
    sendPauseSimulation,
    sendResumeSimulation,
    sendClearSimulation
  };
};

export default useSendControls;
import { useEffect, useRef, useCallback } from 'react';

export const useWebSocket = (url) => {
    const wsRef = useRef(null);
    const nodeEditCallback = useRef(null);

    useEffect(() => {
        wsRef.current = new WebSocket(url);

        wsRef.current.onopen = () => {
            console.log('WebSocket Connected');
        };

        wsRef.current.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type === 'NODE_EDIT' && nodeEditCallback.current) {
                nodeEditCallback.current(data.payload);
            }
        };

        return () => wsRef.current?.close();
    }, [url]);

    const sendMessage = useCallback((type, payload) => {
        if (wsRef.current?.readyState === WebSocket.OPEN) {
            wsRef.current.send(JSON.stringify({ type, payload }));
        }
    }, []);

    const setNodeEditHandler = useCallback((callback) => {
        nodeEditCallback.current = callback;
    }, []);

    return {
        sendMachineCreated: (machine) => sendMessage('MACHINE_CREATED', machine),
        sendQueueCreated: (queue) => sendMessage('QUEUE_CREATED', queue),
        sendEdgeCreated: (edge) => sendMessage('EDGE_CREATED', edge),
        sendClear: () => sendMessage('CLEAR', null),
        sendStartSimulation: (state) => sendMessage('START_SIMULATION', numProducts),
        sendReSimulate: (state) => sendMessage('RESIMULATE', numProducts),
        sendStopSimulation: () => sendMessage('STOP_SIMULATION', null),
        setNodeEditHandler
    };
};
import { useEffect, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';

export const useWebSocket = () => {
    const clientRef = useRef(null);
    const nodeEditCallback = useRef(null);

    useEffect(() => {
        clientRef.current = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            onConnect: () => {
                clientRef.current.subscribe('/topic/simulation', message => {
                    const data = JSON.parse(message.body);
                    if (nodeEditCallback.current) {
                        nodeEditCallback.current(data);
                    }
                });
                clientRef.current.publish({ 
                    destination: '/app/subscribe',
                    body: JSON.stringify({ action: 'subscribe' })
                });
            }
        });

        clientRef.current.activate();
        return () => clientRef.current?.deactivate();
    }, []);

    const sendMessage = useCallback((type, payload) => {
        if (clientRef.current?.connected) {
            clientRef.current.publish({
                destination: '/app/simulation',
                body: JSON.stringify({ type, payload })
            });
        }
    }, []);

    return {
        sendMachineCreated: (machine) => sendMessage('MACHINE_CREATED', machine),
        sendQueueCreated: (queue) => sendMessage('QUEUE_CREATED', queue),
        sendEdgeCreated: (edge) => sendMessage('EDGE_CREATED', edge),
        sendClear: () => sendMessage('CLEAR', null),
        sendStartSimulation: (numProducts) => sendMessage('START_SIMULATION', numProducts),
        sendReSimulate: (numProducts) => sendMessage('RESIMULATE', numProducts),
        sendStopSimulation: () => sendMessage('STOP_SIMULATION', null),
        setNodeEditHandler: (callback) => { nodeEditCallback.current = callback; }
    };
};
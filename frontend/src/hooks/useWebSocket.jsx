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

    return {
        setNodeEditHandler: (callback) => { nodeEditCallback.current = callback; }
    };
};

// {
//     machine: ["M1", "M2"],
//     queue: ["Q1", "Q2"],
//     edges: [{source: "M1", target: "Q2"}]
//     products: 3
// }
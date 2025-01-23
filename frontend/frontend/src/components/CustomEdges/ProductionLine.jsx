import { BaseEdge, getBezierPath, getStraightPath } from '@xyflow/react';
import React from 'react';

// Custom edge component
const ProductionLine = ({
    sourceX,
    sourceY,
    targetX,
    targetY,
    sourcePosition,
    targetPosition,
    id
    }) => {
    const [edgePath] = getBezierPath({
        sourceX,
        sourceY,
        sourcePosition,
        targetX,
        targetY,
        targetPosition,
    });

    return (
        <g>
        <defs>
            <marker
            id={`arrow-${id}`}
            viewBox="0 0 10 10"
            refX="5"
            refY="5"
            markerWidth="3"
            markerHeight="3"
            orient="auto"
            >
            <path d="M 0 0 L 10 5 L 0 10 z" fill="#2563eb" />
            </marker>
        </defs>
        <BaseEdge
            path={edgePath}
            id={id}
            style={{ 
            stroke: '#2563eb', 
            strokeWidth: 3,
            strokeLinecap: 'round'
            }}
            markerEnd={`url(#arrow-${id})`}
        />
        </g>
    );
    };
export default ProductionLine;
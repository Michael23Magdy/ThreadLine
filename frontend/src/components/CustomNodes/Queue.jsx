import { useCallback } from 'react';
import { Handle, Position } from '@xyflow/react';
import { AiFillFunnelPlot } from "react-icons/ai";
import { IDs } from '../../constants/IDs';
 
const Queue = ({ data }) => {
  const onChange = useCallback((evt) => {
    console.log(evt.target.value);
  }, []);
 
  return (
    <>
      {(data.id != IDs.input) && <Handle type="target" position={Position.Left} style={{border: '2px solid black', background: 'white',}} />}
      <div className={`w-16 h-8 relative group rounded-lg border-2 border-solid border-black flex items-center justify-evenly bg-zinc-500 p-2`}>
        <AiFillFunnelPlot /> 
        <span className='text-md font-semibold'>{data.number}</span>  
        <span className='hidden group-hover:block absolute bg-blue-500 text-white top-8 text-xs rounded-md p-1'>{data.id}</span>

      </div>
      {(data.id != IDs.output)&&<Handle type="source" position={Position.Right} id="a" />}
    </>
  );
}
export default Queue
import { useCallback } from 'react';
import { Handle, Position } from '@xyflow/react';
import { MdFactory } from 'react-icons/md';
 
const Machine = ({ data }) => {
  const onChange = useCallback((evt) => {
    console.log(evt.target.value);
  }, []);
 
  return (
    <>
      <Handle type="target" position={Position.Left} style={{border: '2px solid black', background: 'white',}} />
      <div className={`w-12 h-12 relative group rounded-lg border-2 border-solid border-black flex items-center justify-center ${data.active?"bg-yellow-300":"bg-slate-500"}`}>
        <MdFactory className={data.active?"animate-bounce":""} />
        <span className='hidden group-hover:block absolute bg-blue-500 text-white top-12 text-xs rounded-md p-1'>{data.id}</span>
      </div>
      <Handle type="source" position={Position.Right} id="a" />
    </>
  );
}
export default Machine
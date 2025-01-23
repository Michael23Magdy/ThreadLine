import { useCallback, useEffect, useState } from 'react';
import { Handle, Position } from '@xyflow/react';
import { MdFactory } from 'react-icons/md';
import SoundEffect from '../soundEffect/SoundEffect';
 
const Machine = ({ data }) => {
  const [effectQueue, setEffectQueue] = useState([]);

  // Handle activation signal
  useEffect(() => {
    if (!data.active) {
      setEffectQueue((prevQueue) => [...prevQueue, Date.now()]);
    }
  }, [data.active]);

  const removeEffect = useCallback(() => {
    setEffectQueue((prevQueue) => prevQueue.slice(1));
  }, []);


  const onChange = useCallback((evt) => {
    console.log(evt.target.value);
  }, []);
 
  return (
    <>
      <Handle type="target" position={Position.Left} style={{border: '2px solid black', background: 'white',}} />
      <div className={`w-12 h-12 relative group rounded-lg border-2 border-solid border-black flex items-center justify-center 
                      ${data.active?`bg-[${data.color}]`:"bg-slate-500"}`} style={{backgroundColor: data.color}}>
        <MdFactory className={data.active?"animate-bounce":""} />
        <span className='hidden group-hover:block absolute bg-blue-500 text-white top-12 text-xs rounded-md p-1'>{data.id}</span>
      </div>
      {effectQueue.map((key) => (
          <SoundEffect key={key} onComplete={removeEffect} />
        ))}
      <Handle type="source" position={Position.Right} id="a" />
    </>
  );
}
export default Machine
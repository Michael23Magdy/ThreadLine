import { AiFillFunnelPlot } from "react-icons/ai";
import { FaDeleteLeft, FaPlay, FaStop } from "react-icons/fa6";
import { LuRefreshCw } from "react-icons/lu";
import { MdFactory } from "react-icons/md";

import ButtonGroup from "./ButtonGroup/ButtonGroup";
import NumberSelector from "./NumberSelector";


const ToolBar = ({addMachine, addQueue, onClear}) => {
    const components = [
        { icon: MdFactory, text: "Machine", onClickAction:()=> addMachine() },
        { icon: AiFillFunnelPlot, text: "Queue", onClickAction:()=> addQueue() }
    ];
    
    const controls = [
        { icon: FaPlay, text: "", onClickAction:()=> alert("Simulate") },
        { icon: LuRefreshCw, text: "", onClickAction:()=> alert("Resimulate") },
        { icon: FaStop , text: "", onClickAction:()=> alert("Stop") },
        { icon: FaDeleteLeft, text: "", onClickAction:()=> onClear() },
    ];
    return (
        <div className="w-full h-[50px] flex justify-evenly items-center shadow-lg absolute bg-white z-10">
            <h3 className="font-extralight">ThreadLine</h3>
            <NumberSelector />
            <ButtonGroup buttons={components}/>
            <ButtonGroup buttons={controls}/>
        </div>
    )
}
export default ToolBar
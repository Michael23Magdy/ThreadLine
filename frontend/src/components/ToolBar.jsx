import { AiFillFunnelPlot } from "react-icons/ai";
import { FaDeleteLeft, FaPlay, FaStop } from "react-icons/fa6";
import { LuRefreshCw } from "react-icons/lu";
import { MdFactory } from "react-icons/md";
import { useState } from "react";

import ButtonGroup from "./ButtonGroup/ButtonGroup";
import NumberSelector from "./NumberSelector";

const ToolBar = ({addMachine, addQueue, startSimulation, replaySimulation, pauseSimulation, resumeSimulation, clearSimulation}) => {
    const [numProducts, setNumProducts] = useState(1);
    
    const components = [
        { icon: MdFactory, text: "Machine", onClickAction:()=> addMachine() },
        { icon: AiFillFunnelPlot, text: "Queue", onClickAction:()=> addQueue() }
    ];
    
    const controls = [
        { icon: FaPlay, text: "", onClickAction:()=> startSimulation(numProducts) },
        { icon: LuRefreshCw, text: "", onClickAction:()=> replaySimulation(numProducts) },
        { icon: FaStop , text: "", onClickAction:()=> pauseSimulation() },
        { icon: FaDeleteLeft, text: "", onClickAction:()=> clearSimulation() },
    ];

    return (
        <div className="w-full h-[50px] flex flex-wrap justify-evenly items-center shadow-lg absolute bg-white z-10">
            <h3 className="font-extralight">ThreadLine</h3>
            <NumberSelector value={numProducts} onChange={setNumProducts} />
            <ButtonGroup buttons={components}/>
            <ButtonGroup buttons={controls}/>
        </div>
    )
}

export default ToolBar

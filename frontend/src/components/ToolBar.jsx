import { AiFillFunnelPlot } from "react-icons/ai";
import { FaDeleteLeft, FaPlay, FaStop, FaPause, FaRepeat, FaTrash } from "react-icons/fa6";
import { LuRefreshCw } from "react-icons/lu";
import { MdFactory } from "react-icons/md";
import { useState } from "react";

import ButtonGroup from "./ButtonGroup/ButtonGroup";
import NumberSelector from "./NumberSelector";
import { SimulationStates } from "../constants/States";

const ToolBar = ({simulationState, addMachine, addQueue, startSimulation, replaySimulation, pauseSimulation, resumeSimulation, clearSimulation, resetSimulation}) => {
    const [numProducts, setNumProducts] = useState(1);
    
    const components = [
        { icon: MdFactory, text: "Machine", active: simulationState == SimulationStates.compose, onClickAction:()=> addMachine() },
        { icon: AiFillFunnelPlot, text: "Queue", active: simulationState == SimulationStates.compose, onClickAction:()=> addQueue() }
    ];

    const StateToControls = {
        [SimulationStates.compose]: [
            { icon: FaPlay, text: "", active: true, onClickAction:()=> startSimulation(numProducts) },
            { icon: FaRepeat, text: "", active: false, onClickAction:()=> replaySimulation(numProducts) },
            { icon: FaTrash, text: "", active: true, onClickAction:()=> clearSimulation() }
        ],
        [SimulationStates.running]: [
            { icon: FaPause , text: "", active: true, onClickAction:()=> pauseSimulation() },
            { icon: FaRepeat, text: "", active: true, onClickAction:()=> replaySimulation(numProducts) },
            { icon: FaStop, text: "", active: true, onClickAction:()=> resetSimulation() }
        ],
        [SimulationStates.paused]: [
            { icon: FaPlay , text: "", active: true, onClickAction:()=> resumeSimulation() },
            { icon: FaRepeat, text: "", active: true, onClickAction:()=> replaySimulation(numProducts) },
            { icon: FaStop, text: "", active: true, onClickAction:()=> resetSimulation() }
        ]
    };

    return (
        <div className="w-full h-[50px] flex flex-wrap justify-evenly items-center shadow-lg absolute bg-white z-10">
            <h3 className="font-extralight">ThreadLine</h3>
            <NumberSelector value={numProducts} onChange={setNumProducts} />
            <ButtonGroup buttons={components}/>
            <ButtonGroup buttons={StateToControls[simulationState]}/>
        </div>
    )
}

export default ToolBar

import { useState } from "react";
import { FaMinus, FaPlus } from "react-icons/fa6";
import ButtonGroup from "./ButtonGroup/ButtonGroup";
import ButtonComponent from "./ButtonGroup/ButtonComponent";


const NumberSelector = ({})=>{
    const [number, setNumber] = useState(1);
    const increase = () => setNumber(number + 1);
    const decrease = () => setNumber(Math.max(0, number - 1));
    const components = [
        { icon: FaMinus, text: " ", onClickAction:decrease },
        { icon: null, text: number, onClickAction:increase},
        { icon: FaPlus, text: " ", onClickAction:increase }
    ];
    return (
        <div className="inline-flex items-center rounded-md shadow-sm">
            <ButtonComponent
              icon={{icon: FaMinus}.icon}
              position={'left'}
              onClickAction={decrease}
            />
            <div className="text-xs w-16 text-center text-slate-800 hover:text-blue-600 bg-white hover:bg-slate-100 border border-slate-200 font-medium px-4 py-2 inline-flex space-x-1 items-center justify-center">
                {number}
            </div>
            <ButtonComponent
              icon={{icon: FaPlus}.icon}
              position={'right'}
              onClickAction={increase}
            />
        </div>
    )
}
export default NumberSelector
import { FaMinus, FaPlus } from "react-icons/fa6";
import ButtonComponent from "./ButtonGroup/ButtonComponent";

const NumberSelector = ({ value, onChange }) => {
    const increase = () => onChange(value + 1);
    const decrease = () => onChange(Math.max(1, value - 1));

    return (
        <div className="inline-flex items-center rounded-md shadow-sm">
            <ButtonComponent
              icon={{icon: FaMinus}.icon}
              active={value!=1}
              position={'left'}
              onClickAction={decrease}
            />
            <div className="text-xs w-16 text-center text-slate-800 hover:text-blue-600 bg-white hover:bg-slate-100 border border-slate-200 font-medium px-4 py-2 inline-flex space-x-1 items-center justify-center">
                {value}
            </div>
            <ButtonComponent
              active={true}
              icon={{icon: FaPlus}.icon}
              position={'right'}
              onClickAction={increase}
            />
        </div>
    )
}

export default NumberSelector
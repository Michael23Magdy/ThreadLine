
// Button component for individual buttons within the group
const ButtonComponent = ({ icon: Icon, text, position, active, onClickAction }) => {
  const baseClasses = "text-sm bg-white border border-slate-200 font-medium px-4 py-2 inline-flex space-x-1 items-center";
  const activeClasses = "text-slate-800 hover:text-blue-600 hover:bg-slate-100"
  const disabledClasses = "text-gray-400 cursor-not-allowed";
  const activeButton = active ? activeClasses : disabledClasses;
  let positionClasses = "";
  if (position === "left") {
    positionClasses = "rounded-l-lg border-r-0";
  } else if (position === "right") {
    positionClasses = "rounded-r-lg border-l-0";
  } else {
    positionClasses = "border-x-0";
  }

  return (
    <button
      className={`${baseClasses} ${positionClasses} ${activeButton}`}
      onClick={active ? onClickAction : null}
      disabled={!active}
    >
      <span>
        {Icon && <Icon className="w-4 h-4" />}
      </span>
      <span className="hidden md:inline-block">{text}</span>
    </button>
  );
};
export default ButtonComponent
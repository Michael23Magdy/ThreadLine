import ButtonComponent from "./ButtonComponent";

const ButtonGroup = ({ buttons }) => {
    return (
      <div className="inline-flex items-center rounded-md shadow-sm">
        {buttons.map((button, index) => {
          let position;
          if (index === 0) position = "left";
          else if (index === buttons.length - 1) position = "right";
          else position = "middle";
  
          return (
            <ButtonComponent
              key={index}
              icon={button.icon}
              text={button.text}
              position={position}
              active={button.active}
              onClickAction={button.onClickAction}
            />
          );
        })}
      </div>
    );
};
export default ButtonGroup
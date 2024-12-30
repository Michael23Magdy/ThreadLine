import { useEffect } from "react";
import { PiUserSoundDuotone } from "react-icons/pi";


const SoundEffect = ({ onComplete }) => {
  useEffect(() => {
    const playAudio = async () => {
      try {
        const audio = new Audio('/done.mp3'); // Absolute path to public folder
        audio.volume = 0.1;
        await audio.play(); // Ensure promise resolves for modern browsers
      } catch (error) {
        console.error('Error playing audio:', error);
      }
    };

    playAudio();

    const timeout = setTimeout(() => {
      onComplete(); // Notify parent when effect completes
    }, 2000); // Duration of the effect

    return () => {
      clearTimeout(timeout);
    };
  }, [onComplete]);

  return (
    <div className="absolute -top-5 left-4 w-4 h-4 flex items-center justify-center">
      <span className="text-blue text-sm"><PiUserSoundDuotone /></span>
    </div>
  );
};

export default SoundEffect;

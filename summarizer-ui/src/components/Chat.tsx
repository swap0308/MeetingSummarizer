import { useRef } from "react"

const randomWords = [
  "apple", "banana", "cherry", "delta", "echo", "forest", "galaxy", "horizon",
  "island", "jungle", "kite", "lemon", "mountain", "nebula", "ocean", "pearl",
  "quartz", "river", "sunset", "tiger", "umbrella", "violet", "whisper", "xenon",
  "yacht", "zephyr","apple", "banana", "cherry", "delta", "echo", "forest", "galaxy", "horizon",
  "island", "jungle", "kite", "lemon", "mountain", "nebula", "ocean", "pearl",
  "quartz", "river", "sunset", "tiger", "umbrella", "violet", "whisper", "xenon",
  "yacht", "zephyr","apple", "banana", "cherry", "delta", "echo", "forest", "galaxy", "horizon",
  "island", "jungle", "kite", "lemon", "mountain", "nebula", "ocean", "pearl",
  "quartz", "river", "sunset", "tiger", "umbrella", "violet", "whisper", "xenon",
  "yacht", "zephyr"
];

export default function Chat() {
    const inputRef = useRef<HTMLInputElement | null>(null);

    const handleClick = () => {
        if(inputRef.current) {
            inputRef.current.click();
        }
    }

    return (
        <div className="h-[100%] flex-1 flex flex-col">
            <div id="summarized-content">{randomWords.map(word => (<div>{word}</div>))}</div>
            <button className="file-picker-container px-[12px] py-[8px] border-[#7F8CAA] border-1 rounded-[2px] text-white" onClick={handleClick}>Pick a meeting</button>
            <input className="sr-only" ref={inputRef} type="file" />
        </div>
    )
}
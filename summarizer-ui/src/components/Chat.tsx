import { useRef } from "react"

export default function Chat() {
    const inputRef = useRef<HTMLInputElement | null>(null);

    const handleClick = () => {
        if(inputRef.current) {
            inputRef.current.click();
        }
    }

    return (
        <div className="h-[100%] flex-1 flex flex-col gap-[10px]">
            <div id="summarized-content"></div>
            <div className="flex justify-center">
                <button className="file-picker-container px-[12px] py-[8px] border-[#7F8CAA] border-1 rounded-[2px] text-white w-full lg:w-[20%]" onClick={handleClick}>Pick a meeting</button>
            </div>
            <input className="sr-only" ref={inputRef} type="file" />
        </div>
    )
}
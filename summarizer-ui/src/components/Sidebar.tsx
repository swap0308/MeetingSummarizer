import { useRef } from "react";

export default function Sidebar() {
    const scrollRef = useRef<HTMLUListElement | null>(null);
    const meetings = [];

    const Meeting = ({id, title}: {id: string, title: string}) => {

        const itemRef = useRef<HTMLLIElement | null>(null);
        const titleWidth = itemRef.current?.clientWidth;
        return (
            <li ref={itemRef} key={id} className="flex gap-[8px] justify-between">
                <span title={title} className={`max-w-[calc(${titleWidth} - 32px)] truncate`}>{title}</span>
                <div className="size-[24px]">
                    <img src="./info-circle-svgrepo-com.svg" />
                </div>
            </li>
        );
    };

    return (
        <div className="sidebar w-[30%] lg:w-[400px] mb-[52px]">
            <ul ref={scrollRef} className="py-[20px] px-[12px]">
                {(meetings?.length === 0) ? <Meeting id="jhjh" title="First meeting efsdfs sdfsf sd sdfsdf sd fsdfsf sdf fsdfs" /> : <span>No meetings summarized yet. Get started!</span>}
            </ul>
        </div>
    )
}
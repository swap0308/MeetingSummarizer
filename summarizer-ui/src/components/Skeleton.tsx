export default function Skeleton({ height }: { height: number }) {
    return (
        <div className={`skeleton h-[${height}px]`} />
    );
}
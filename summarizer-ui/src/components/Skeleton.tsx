export default function Skeleton({ height }: { height: number }) {
    return (
        <div className={`skeleton`} style={{ height }}/>
    );
}
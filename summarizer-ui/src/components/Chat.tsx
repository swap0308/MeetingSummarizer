import { useCallback, useRef, useState } from "react";
import ConfirmationModal from "./ConfirmationModal";
import Skeleton from "./Skeleton";

type Summaries = {
  isLoading: boolean,
  id?: string,
  summarizedText?: string,
  title?: string,
  actionItems?: string[] | null,
  createdAt?: string
};

export default function Chat() {
  const inputRef = useRef<HTMLInputElement | null>(null);
  const modlRef = useRef<HTMLDialogElement | null>(null);
  const contentRef = useRef<HTMLDivElement | null>(null);

  const [file, setFile] = useState<File | null>(null);
  const [thumbnail, setThumbnail] = useState<string | null>(null);
  const [fileTitle, setFileTitle] = useState<string | null>(null);
  const [summaries, setSummaries] = useState<Summaries[]>([]);

  const handleClick = () => {
    inputRef.current?.click();
  };

  const handleSubmit = () => {
    if (!file) return;

    setSummaries(prevState => [
      ...prevState,
      { isLoading: true }
    ]);


    const formData = new FormData();
    formData.append("file", file);

    fetch("http://localhost:8090/api/transcribe/getSummarizedText", {
      method: "POST",
      body: formData,
    })
      .then((res) => res.json())
      .then((res) => {
        setSummaries(prevState => [
          ...prevState.slice(0, -1),
          { ...res, isLoading: false }
        ]);
      })
      .catch((err) => console.error("Upload failed:", err))
      .finally(() => { modlRef.current?.close() });
  };


  const showThumbnail = (videoURL: string, title: string) => {
    const video = document.createElement("video");
    video.src = videoURL;
    video.muted = true;
    video.playsInline = true;

    video.addEventListener("loadeddata", () => {
      video.currentTime = 1;
    });

    video.addEventListener("seeked", () => {
      const canvas = document.createElement("canvas");
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;

      const ctx = canvas.getContext("2d");
      ctx?.drawImage(video, 0, 0, canvas.width, canvas.height);

      const imageURL = canvas.toDataURL("image/png");
      setThumbnail(imageURL);
      setFileTitle(title);

      URL.revokeObjectURL(videoURL);
    });
  };

  const handleFilePick = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0];
    if (!selectedFile) return;

    const videoURL = URL.createObjectURL(selectedFile);
    showThumbnail(videoURL, selectedFile.name);
    setFile(selectedFile);

    modlRef.current?.open();
  };

  return (
    <div className="h-[100%] flex-1 flex flex-col gap-[10px]">
      <ConfirmationModal
        ref={modlRef}
        handleSubmit={handleSubmit}
        thumbnail={thumbnail || undefined}
        fileTitle={fileTitle || undefined}
      />

      <div ref={contentRef} id="summarized-content">
        {summaries?.length ? (summaries.map(summary => (
          summary.isLoading ? <Skeleton height={300} /> : (
            <div id={summary.id}>
              <h2>{summary.title}</h2>
              <p>{summary.summarizedText}</p>
              {summary.actionItems?.length &&
                (<>
                  <h3>Action items</h3>
                  {summary.actionItems?.map(item => (
                    <li>{item}</li>
                  ))}
                </>)
              }
            </div>
          )
        ))) : <h3 className="text-white text-[32px]">Start summarizing your meetings!</h3>}
      </div>

      <div className="flex justify-center">
        <button
          className="file-picker-container px-[12px] py-[8px] border-[#7F8CAA] border-1 rounded-[2px] text-white w-full lg:w-[20%]"
          onClick={handleClick}
        >
          Pick a meeting
        </button>
      </div>

      <input
        className="sr-only"
        onChange={handleFilePick}
        ref={inputRef}
        type="file"
        accept="video/*, audio/*"
      />
    </div>
  );
}

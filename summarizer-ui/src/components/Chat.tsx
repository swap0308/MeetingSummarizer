import { useCallback, useRef, useState } from "react";
import ConfirmationModal from "./ConfirmationModal";

export default function Chat() {
  const inputRef = useRef<HTMLInputElement | null>(null);
  const modlRef = useRef<HTMLDialogElement | null>(null);
  const contentRef = useRef<HTMLDivElement | null>(null);

  const [file, setFile] = useState<File | null>(null);
  const [thumbnail, setThumbnail] = useState<string | null>(null);
  const [fileTitle, setFileTitle] = useState<string | null>(null);

  const handleClick = () => {
    inputRef.current?.click();
  };

  const handleSubmit = useCallback(() => {
    if (!file) return;

    const formData = new FormData();
    formData.append("video", file);
    formData.append("timestamp", String(Date.now()));

    fetch("http://localhost:8080/getSummarizedText", {
      method: "POST",
      body: formData,
    })
      .then((res) => res.json())
      .then((res) => {
        console.log("Upload success:", res);
        if (contentRef.current) {
          contentRef.current.innerText = JSON.stringify(res, null, 2);
        }
      })
      .catch((err) => console.error("Upload failed:", err));
  }, [file]);

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

      <div ref={contentRef} id="summarized-content"></div>

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
        accept="video/*"
      />
    </div>
  );
}

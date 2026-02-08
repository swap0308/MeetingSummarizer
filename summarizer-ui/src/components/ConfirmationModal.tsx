import { createPortal } from "react-dom";
import { useImperativeHandle, useRef, forwardRef } from "react";

interface ConfirmationModalProps {
  handleSubmit: () => void;
  thumbnail?: string;
  fileTitle?: string;
}

const ConfirmationModal = forwardRef<HTMLDialogElement, ConfirmationModalProps>(
  ({ handleSubmit, thumbnail, fileTitle }, ref) => {
    const modalRef = useRef<HTMLDialogElement | null>(null);

    useImperativeHandle(ref, () => ({
      open() {
        modalRef.current?.showModal();
      },
      close() {
        modalRef.current?.close();
      },
    }));

    const handleClose = () => {
      modalRef.current?.close();
    };

    const handleChange = () => {
      // you can wire this to re‑open file picker
    };

    return createPortal(
      <dialog
        id="confirmation-modal"
        ref={modalRef}
        className="h-[100vh] w-[100vw] bg-transparent"
      >
        <div className="flex flex-col justify-center h-[100%]">
          <div className="flex flex-col gap-[20px] bg-black w-[50vw] border-2 text-white my-auto mx-auto p-[20px]">
            <div id="preview">
              {thumbnail && (
                <>
                  <img
                    className="h-[80%]"
                    src={thumbnail}
                    alt="Video thumbnail preview"
                  />
                  <h2>{fileTitle}</h2>
                </>
              )}
            </div>
            <div
              id="modal-buttons-container"
              className="flex justify-between"
            >
              <div>
                <button onClick={handleSubmit}>Summarize</button>
              </div>
              <div className="flex gap-[20px]">
                <button onClick={handleChange}>Change</button>
                <button onClick={handleClose}>Close</button>
              </div>
            </div>
          </div>
        </div>
      </dialog>,
      document.body
    );
  }
);

export default ConfirmationModal;

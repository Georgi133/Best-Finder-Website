import { useState } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";

export const useMultiPartForm = (initialValues, onSubmitHandler) => {

  const [formValues, setFormValues] = useState(initialValues);
  const [selectedFile, setSelectedFile] = useState();
  const { setServerErrors } = useTorrentContext();

  const onChangeHandler = (e) => {
    setServerErrors({})
    setFormValues((state) => ({ ...state, [e.target.name]: e.target.value }));
  }

  const onFileSelectedHandler = (e) => {
    if (e.target.files[0] && e.target.files[0].name !== "") {
        setFormValues((state) => ({ ...state, [e.target.name]: e.target.value }));
      setSelectedFile(e.target.files[0]);
    }
  };

  const onSubmit = async () => {
   
    // setFormValues((state) => ({ ...state, [e.target.name]: e.target.value }));

    const { file, ...dtoValues } = formValues;

    const formData = new FormData();
    formData.append("file", selectedFile);
    formData.append(
      "dto",
      new Blob([JSON.stringify(dtoValues)], {
        type: "application/json",
      })
    );

    // formData.append("dto", JSON.stringify(dtoValues));
    /* console.log("New form Data");

    for (var key of formData.entries()) {
      console.log(key[0] + ", " + key[1]);
    }*/
   await onSubmitHandler(formData, formValues.torrent);

  };

  return {
    formValues,
    selectedFile,
    onChangeHandler,
    onFileSelectedHandler,
    onSubmit,
  };
};

import { useState } from "react";

export const useMultiPartForm = (initialValues, onSubmitHandler) => {

  const [formValues, setFormValues] = useState(initialValues);
  const [selectedFile, setSelectedFile] = useState();

  const onChangeHandler = (e) => {
    setFormValues((state) => ({ ...state, [e.target.name]: e.target.value }));
  }

  const onFileSelectedHandler = (e) => {
    if (e.target.files[0] && e.target.files[0].name !== "") {
        setFormValues((state) => ({ ...state, [e.target.name]: e.target.value }));
      setSelectedFile(e.target.files[0]);
    }
  };

  const onSubmit = (e) => {
    const form = e.currentTarget;
    if (form.checkValidity() === false) {
      e.preventDefault();
      e.stopPropagation();
    }
    e.preventDefault();
    setFormValues((state) => ({ ...state, [e.target.name]: e.target.value }));

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
    onSubmitHandler(formData);
  };

  return {
    formValues,
    selectedFile,
    onChangeHandler,
    onFileSelectedHandler,
    onSubmit,
  };
};

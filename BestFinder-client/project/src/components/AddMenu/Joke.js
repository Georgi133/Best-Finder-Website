import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Categories.module.css";
import { useMultiPartForm } from "../hooks/useMultiPartForm";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useTranslation } from "react-i18next";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useEffect, useState } from "react";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";

export const Joke = ({
    torrent,
    setValueCategory,
}) => {
  const { t } = useTranslation();
  const { onTorrentSubmit, isTorrentAdded, setIsTorrentAdded, setAddedMessage, errorMessage, serverErrors, setErrorMessage } = useTorrentContext();
  const [valid, isValid] = useState(false);
  const [torrentValidationInfo, setTorrentValidationInfo] = useState({});
  const { jokeValidate } = useValidatorContext();
  
    const {onFileSelectedHandler, formValues, onChangeHandler, onSubmit } = useMultiPartForm(
      {
        torrent: torrent,
        torrentName: "",
        text: "",
  
        file: "",
      },onTorrentSubmit );

      useEffect(() => {
        if(valid) {
          onSubmit();
          isValid(false);
         };
      }, [valid]);
    
      useEffect(() => {
        setErrorMessage(null);
        if(isTorrentAdded) {
          setValueCategory('category')
          setAddedMessage(true);
         };
      }, [isTorrentAdded]);
    
      useEffect(() => {
        return () => {
          setIsTorrentAdded(false)
        };
      }, []);
    
      const onSubmitJoke = async (e) => {
        e.preventDefault();
       const result = await jokeValidate(formValues);
       setTorrentValidationInfo(result);
       isValid(Object.keys(result).length === 0);
       setErrorMessage(null);
      }
  
    return(
        <>
        {errorMessage && <ErrorMessage message={errorMessage} />}
        <form encType="multipart/form-data" className={style.formContainer} onSubmit={onSubmitJoke}>
        <div className={style.innerContainer + " form-group"}>
          <label className={style.lyrics} htmlFor="torrentName">
          {t("jokeAdd.name")}
          </label>
          <input
            value={formValues.torrentName}
            onChange={onChangeHandler}
            type="text"
            id="torrentName"
            name="torrentName"
            placeholder="Joke Name"
            required
            className={style.inp + " form-control"}
          />
           {torrentValidationInfo.torrentName ? <ErrorMessage message={torrentValidationInfo.torrentName}/> : serverErrors.torrentName ?
            <ErrorMessage message={serverErrors.torrentName}/> : ''}
        </div>

        <div className={style.innerContainer + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="text">
            {t("jokeAdd.text")}
          </label>
          <textarea
            value={formValues.text}
            onChange={onChangeHandler}
            type="text"
            id="text"
            name="text"
            required
            placeholder="Add Content of the Joke"
            className={style.inp + " form-control"}
          />
          {torrentValidationInfo.text ? <ErrorMessage message={torrentValidationInfo.text}/> : serverErrors.text ?
            <ErrorMessage message={serverErrors.text}/> : ''}
        </div>

            <div className={style.picture + " form-group"}>
              <label className={style.pictureLabel} htmlFor="pic">
                {t("torrentCommon.fileUpload")}
              </label>
              <input
                className={style.pictureInput}
                value={formValues.picture}
                onChange={onFileSelectedHandler}
                name="file"
                id="pic"
                type="file"
                required
              />
              { torrentValidationInfo && <ErrorMessage message={torrentValidationInfo.file}/>}
            </div>
          <ButtonSubmit />
      </form>
        </>
        )
}
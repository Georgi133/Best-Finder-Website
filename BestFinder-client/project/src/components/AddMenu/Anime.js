import { useEffect, useState } from "react";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Categories.module.css";
import { InnerCategoriesOptions } from "./InnerCategoriesOptions";
import { CategoriesOptions } from "./CategoriesOptions";
import { useMultiPartForm } from "../hooks/useMultiPartForm";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useTranslation } from "react-i18next";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";

export const Anime = ({ torrent,setValueCategory }) => {
  const { t } = useTranslation();
  const [rowForCategories, setRowForCategories] = useState([1]);
  const [countCategory, setCountCategory] = useState("categories1");

  const { onTorrentSubmit, isTorrentAdded, setIsTorrentAdded, setAddedMessage, errorMessage, serverErrors, setErrorMessage } = useTorrentContext();
  const [valid, isValid] = useState(false);
  const [torrentValidationInfo, setTorrentValidationInfo] = useState({});
  const { gameValidate } = useValidatorContext();

  const {onFileSelectedHandler, formValues, onChangeHandler, onSubmit } = useMultiPartForm(
    {
      torrent: torrent,
      torrentName: "",
      torrentResume: "",
      releasedYear:"",
      trailer:"",

      category1: "",
      category2: "",
      category3: "",
      file: "",
    },onTorrentSubmit );

  const onChangeCategories = (e) => {
    const value = e.target.value;
    if (value === "categories1") {
      setRowForCategories([1]);
    } else if (value === "categories2") {
      setRowForCategories([1, 2]);
    } else if (value === "categories3") {
      setRowForCategories([1, 2, 3]);
    }
    setCountCategory(value);
  };

  const onInnerCategoryChange = (e) => {
    const value = e.target.value;
    e.target.value = value;
    onChangeHandler(e);
  };

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

  const onSubmitAnime = async (e) => {
    e.preventDefault();
   const result = await gameValidate(formValues);
   setTorrentValidationInfo(result);
   isValid(Object.keys(result).length === 0);
   setErrorMessage(null)
  }

  return (
    <>
    {errorMessage && <ErrorMessage message={errorMessage} />}
      <form encType="multipart/form-data" className={style.formContainer} onSubmit={onSubmitAnime}>
        <div className={style.innerContainer + " form-group"}>
          <label className={style.lyrics} htmlFor="torrentName">
            {t("animeAdd.name")}
          </label>
          <input
            value={formValues.torrentName}
            onChange={onChangeHandler}
            type="text"
            id="torrentName"
            name="torrentName"
            placeholder="Anime Name"
            required
            className={style.inp + " form-control"}
          />
          {torrentValidationInfo.torrentName ? <ErrorMessage message={torrentValidationInfo.torrentName}/> : serverErrors.torrentName ?
            <ErrorMessage message={serverErrors.torrentName}/> : ''}
        </div>

        <div className={style.innerContainer + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="resume">
          {t("torrentCommon.resume")}
          </label>
          <textarea
            value={formValues.torrentResume}
            onChange={onChangeHandler}
            type="text"
            id="resume"
            name="torrentResume"
            required
            placeholder="Short resume of the anime"
            className={style.inp + " form-control"}
          />
          {torrentValidationInfo.torrentResume ? <ErrorMessage message={torrentValidationInfo.torrentResume}/> : serverErrors.torrentResume ?
            <ErrorMessage message={serverErrors.torrentResume}/> : ''}
        </div>

        <div className={style.innerContainer + " form-group"}>
          <label className={style.lyrics} htmlFor="releasedYear">
          {t("movieAndSongAdd.year")}
          </label>
          <input
            value={formValues.releasedYear}
            onChange={onChangeHandler}
            type="number"
            min="1900"
            max="2024"
            step="1"
            id="releasedYear"
            name="releasedYear"
            placeholder="2004"
            required
            className={style.inp + " form-control"}
          />
          {torrentValidationInfo.releasedYear ? <ErrorMessage message={torrentValidationInfo.releasedYear}/> : serverErrors.releasedYear ?
            <ErrorMessage message={serverErrors.releasedYear}/> : ''}
        </div>

        <div className={style.innerContainer + " form-group"}>
          <label className={style.lyrics} htmlFor="trailer">
            Trailer:
          </label>
          <input
            value={formValues.trailer}
            onChange={onChangeHandler}
            type="text"
            id="trailer"
            name="trailer"
            placeholder="Link of trailer"
            required
            className={style.inp + " form-control"}
          />
          {torrentValidationInfo.trailer ? <ErrorMessage message={torrentValidationInfo.trailer}/> : serverErrors.trailer ?
            <ErrorMessage message={serverErrors.trailer}/> : ''}
        </div>
        
        <div className={style.innerContainer + " " + style.categoryContainer}>
          <select
            value={countCategory}
            onChange={onChangeCategories}
            className={style.selectContainer}
          >
            <CategoriesOptions />
          </select>
          <div>
            {rowForCategories.map((countCat) => {
              return (
                <select
                  key={countCat}
                  onChange={onInnerCategoryChange}
                  className={style.innerCategory}
                  name={`category${countCat}`}
                >
                  <InnerCategoriesOptions />
                </select>
              );
            })}
            {torrentValidationInfo.category ? <ErrorMessage message={torrentValidationInfo.category}/> : serverErrors.category ?
            <ErrorMessage message={serverErrors.category}/> : ''}
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
        </div>
        <ButtonSubmit />

      </form>
    </>
  );
};

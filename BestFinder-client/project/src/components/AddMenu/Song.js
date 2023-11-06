import { InnerSongCategoriesOptions } from "./InnerSongCategoriesOptions";
import { useEffect, useState } from "react";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Categories.module.css";
import { CategoriesOptions } from "./CategoriesOptions";
import { useMultiPartForm } from "../hooks/useMultiPartForm";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { SingerOptions } from "./SingerOptions";
import { useTranslation } from "react-i18next";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";

export const Song = ({
    torrent,
    setValueCategory,
}) => {
  const { t } = useTranslation();
    const [rowForCategories, setRowForCategories] = useState([1]);
    const [rowForSingers, setRowForSingers] = useState([1]);
    const [countSinger, setCountSinger] = useState("singer1");
    const [countCategory, setCountCategory] = useState("categories1");
  
    const { onTorrentSubmit, isTorrentAdded, setIsTorrentAdded, setAddedMessage, errorMessage, serverErrors, setErrorMessage } = useTorrentContext();
    const [valid, isValid] = useState(false);
    const [torrentValidationInfo, setTorrentValidationInfo] = useState({});
    const { songValidate } = useValidatorContext();
  
    const { onFileSelectedHandler, formValues, onChangeHandler, onSubmit } =
      useMultiPartForm(
        {
          torrent: torrent,
          torrentName: "",
          torrentResume: "",
          releasedYear: "",
          songVideo:"",
  
          singer1: "",
          singer2: "",
          singer3: "",
          singer4: "",
          singer5: "",
  
          category1: "",
          category2: "",
          category3: "",
          file: "",
        },
        onTorrentSubmit
      );
  
    const onChangeCountActors = (e) => {
      const value = e.target.value;
      if (value === "singer1") {
        setRowForSingers([1]);
      } else if (value === "singer2") {
        setRowForSingers([1, 2]);
      } else if (value === "singer3") {
        setRowForSingers([1, 2, 3]);
      } else if (value === "singer4") {
        setRowForSingers([1, 2, 3, 4]);
      } else if (value === "singer5") {
        setRowForSingers([1, 2, 3, 4, 5]);
      }
      setCountSinger(value);
    };
  
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
  
    const onSubmitSong = async (e) => {
      e.preventDefault();
     const result = await songValidate(formValues);
     setTorrentValidationInfo(result);
     isValid(Object.keys(result).length === 0);
     setErrorMessage(null);
    }
  
    

    return(
        <>
        {errorMessage && <ErrorMessage message={errorMessage} />}
        <form
        encType="multipart/form-data"
        className={style.formContainer}
        onSubmit={onSubmitSong}
      >
        <div className={style.innerContainer + " form-group"}>
          <label className={style.lyrics} htmlFor="torrentName">
            {t("songAdd.name")}
          </label>
          <input
            value={formValues.torrentName}
            onChange={onChangeHandler}
            type="text"
            id="torrentName"
            name="torrentName"
            placeholder="Song Name"
            required
            className={style.inp + " form-control"}
          />
          {torrentValidationInfo.torrentName ? <ErrorMessage message={torrentValidationInfo.torrentName}/> : serverErrors.torrentName ?
            <ErrorMessage message={serverErrors.torrentName}/> : ''}
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
          <label className={style.lyrics} htmlFor="songVideo">
            Song Video Link:
          </label>
          <input
            value={formValues.songVideo}
            onChange={onChangeHandler}
            type="text"
            id="songVideo"
            name="songVideo"
            placeholder="Song Link"
            required
            className={style.inp + " form-control"}
          />
           {torrentValidationInfo.songVideo ? <ErrorMessage message={torrentValidationInfo.songVideo}/> : serverErrors.songVideo ?
            <ErrorMessage message={serverErrors.songVideo}/> : ''}
        </div>

        <div className={style.innerContainer + " " + style.actorContainer}>
          <select value={countSinger} onChange={onChangeCountActors}>
            <SingerOptions />
          </select>

          {rowForSingers.map((countOfRow) => {
            return (
              <div key={countOfRow} className={style.innerActorContainer}>
                <label className={style.lyrics} htmlFor={`singer${countOfRow}`}>
                  Singer {countOfRow}:
                </label>
                <input
                  value={
                    countOfRow === 1
                      ? formValues.singer1
                      : countOfRow === 2
                      ? formValues.singer2
                      : countOfRow === 3
                      ? formValues.singer3
                      : countOfRow === 4
                      ? formValues.singer4
                      : formValues.singer5
                  }
                  onChange={onChangeHandler}
                  id={`singer${countOfRow}`}
                  name={`singer${countOfRow}`}
                  type="text"
                  placeholder="First and Last Name"
                  required
                  className={style.inp + " form-control"}
                />
                {torrentValidationInfo.singer ? <ErrorMessage message={torrentValidationInfo.singer}/> : serverErrors.singer ?
            <ErrorMessage message={serverErrors.singer}/> : ''}
                
              </div>
            );
          })}
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
                  <InnerSongCategoriesOptions />
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
        
        )
}
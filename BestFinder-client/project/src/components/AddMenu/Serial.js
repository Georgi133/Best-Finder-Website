import { useEffect, useState } from "react";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Categories.module.css";
import { InnerCategoriesOptions } from "./InnerCategoriesOptions";
import { CategoriesOptions } from "./CategoriesOptions";
import { ActorOptions } from "./ActorOptions";
import { useMultiPartForm } from "../hooks/useMultiPartForm";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useTranslation } from "react-i18next";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";

export const Serial = ({
    torrent,
    setValueCategory,
}) => {

    const { t } = useTranslation();
    const [rowForCategories, setRowForCategories] = useState([1]);
    const [rowForActors, setRowForActor] = useState([1]);
    const [countActor, setCountActor] = useState("actor1");
    const [countCategory, setCountCategory] = useState("categories1");
    const [valid, isValid] = useState(false);
    const [torrentValidationInfo, setTorrentValidationInfo] = useState({});
  
    const { onTorrentSubmit, isTorrentAdded, setIsTorrentAdded, setAddedMessage, errorMessage, serverErrors, setErrorMessage } = useTorrentContext();
    const { serialValidate } = useValidatorContext();
  
    const { onFileSelectedHandler, formValues, onChangeHandler, onSubmit } =
      useMultiPartForm(
        {
          torrent: torrent,
          torrentName: "",
          torrentResume: "",
          seasons: "",
          trailer: "",
  
          actor1: "",
          actor2: "",
          actor3: "",
          actor4: "",
          actor5: "",
  
          category1: "",
          category2: "",
          category3: "",
          file: "",
        },
        onTorrentSubmit
      );
  
    const onChangeCountActors = (e) => {
      const value = e.target.value;
      if (value === "actor1") {
        setRowForActor([1]);
      } else if (value === "actor2") {
        setRowForActor([1, 2]);
      } else if (value === "actor3") {
        setRowForActor([1, 2, 3]);
      } else if (value === "actor4") {
        setRowForActor([1, 2, 3, 4]);
      } else if (value === "actor5") {
        setRowForActor([1, 2, 3, 4, 5]);
      }
      setCountActor(value);
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
      setErrorMessage(null);
      if(isTorrentAdded) {
        setValueCategory('category')
        setAddedMessage(true);
       };
    }, [isTorrentAdded]);
  
    useEffect(() => {
      return () => {
        setIsTorrentAdded(false);
      };
    }, []);


    const onSubmitSerial = async (e) => {
      e.preventDefault();
     const result = await serialValidate(formValues);
     setTorrentValidationInfo(result);
     isValid(Object.keys(result).length === 0);
     setErrorMessage(null)
    }
  
    return (
      <>
      {errorMessage && <ErrorMessage message={errorMessage} />}
        <form
          encType="multipart/form-data"
          className={style.formContainer}
          onSubmit={onSubmitSerial}
        >
          <div className={style.innerContainer + " form-group"}>
            <label className={style.lyrics} htmlFor="torrentName">
              {t("serialAdd.name")}
            </label>
            <input
              value={formValues.torrentName}
              onChange={onChangeHandler}
              type="text"
              id="torrentName"
              name="torrentName"
              placeholder="Serial Name"
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
              placeholder="Short resume of the serial"
              className={style.inp + " form-control"}
            />
            {torrentValidationInfo.torrentResume ? <ErrorMessage message={torrentValidationInfo.torrentResume}/> : serverErrors.torrentResume ?
            <ErrorMessage message={serverErrors.torrentResume}/> : ''}
          </div>
  
          <div className={style.innerContainer + " form-group"}>
            <label className={style.lyrics} htmlFor="seasons">
            {t("serialAdd.seasons")}
            </label>
            <input
              value={formValues.seasons}
              onChange={onChangeHandler}
              type="number"
              min="1"
              max="100"
              step="1"
              id="seasons"
              name="seasons"
              required
              className={style.inp + " form-control"}
            />
            {torrentValidationInfo.seasons ? <ErrorMessage message={torrentValidationInfo.seasons}/> : serverErrors.seasons ?
            <ErrorMessage message={serverErrors.seasons}/> : ''}
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
  
          <div className={style.innerContainer + " " + style.actorContainer}>
            <select value={countActor} onChange={onChangeCountActors}>
              <ActorOptions />
            </select>
  
            {rowForActors.map((countOfRow) => {
              return (
                <div key={countOfRow} className={style.innerActorContainer}>
                  <label className={style.lyrics} htmlFor={`actor${countOfRow}`}>
                    Actor {countOfRow}:
                  </label>
                  <input
                    value={
                      countOfRow === 1
                        ? formValues.actor1
                        : countOfRow === 2
                        ? formValues.actor2
                        : countOfRow === 3
                        ? formValues.actor3
                        : countOfRow === 4
                        ? formValues.actor4
                        : formValues.actor5
                    }
                    onChange={onChangeHandler}
                    id={`actor${countOfRow}`}
                    name={`actor${countOfRow}`}
                    type="text"
                    placeholder="First and Last Name"
                    required
                    className={style.inp + " form-control"}
                  />
                  {torrentValidationInfo.actor ? <ErrorMessage message={torrentValidationInfo.actor}/> : serverErrors.actor ?
            <ErrorMessage message={serverErrors.actor}/> : ''}
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
        
        )
}
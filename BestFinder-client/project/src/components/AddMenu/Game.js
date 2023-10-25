import { useState } from "react";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Categories.module.css";
import { InnerCategoriesOptions } from "./InnerCategoriesOptions";
import { CategoriesOptions } from "./CategoriesOptions";
import { useAdminContext } from "../AdminContext/AdminContext";
import { useMultiPartForm } from "../hooks/useMultiPartForm";

export const Game = ({
    torrent,
}) => {
    
    const [rowForCategories, setRowForCategories] = useState([1]);
    const [countCategory, setCountCategory] = useState("categories1");
  
    const { onTorrentSubmit } = useAdminContext();
  
    const {onFileSelectedHandler, formValues, onChangeHandler, onSubmit } = useMultiPartForm(
      {
        torrent: torrent,
        torrentName: "",
        torrentResume: "",
        releasedYear:"",
  
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
    return(
        <>
        <form encType="multipart/form-data" className={style.formContainer} onSubmit={onSubmit}>
        <div className={style.innerContainer + " form-group"}>
          <label className={style.lyrics} htmlFor="movieName">
            Movie Name:
          </label>
          <input
            value={formValues.torrentName}
            onChange={onChangeHandler}
            type="text"
            id="movieName"
            name="torrentName"
            placeholder="Movie Name"
            required
            className={style.inp + " form-control"}
          />
        </div>

        <div className={style.innerContainer + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="resume">
            resume:
          </label>
          <textarea
            value={formValues.torrentResume}
            onChange={onChangeHandler}
            type="text"
            id="resume"
            name="torrentResume"
            required
            placeholder="Short resume of the movie"
            className={style.inp + " form-control"}
          />
        </div>

        <div className={style.innerContainer + " form-group"}>
          <label className={style.lyrics} htmlFor="releasedYear">
            Released Year:
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
          </div>
            <div className={style.picture + " form-group"}>
              <label className={style.pictureLabel} htmlFor="pic">
                Upload picture:
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
            </div>
          <ButtonSubmit />
        </div>
      </form>
        </>
        )
}
import { useState } from "react";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./AddMovie.module.css";
import { Anime } from "./Anime";
import { MenuFinder } from "./MenuFinder";

export const AddMenu = () => {
  const [valueCategory, setValueCategory] = useState("category");

  const onChangeCategory = (e) => {
    const value = e.target.value;
    setValueCategory(value);
  };

  return (
    <>
      <MyNavBar />
      <div className={style.container}>
        <h2 className={style.header}>Add Menu</h2>
        <form>
          <select
            value={valueCategory}
            onChange={onChangeCategory}
            className={style.selectContainer}
          >
            <option hidden value="category">
              Category
            </option>
            <option className={style.opt} value="movie">
              Add Movie
            </option>
            <option className={style.opt} value="serial">
              Add Serial
            </option>
            <option className={style.opt} value="game">
              Add Game
            </option>
            <option className={style.opt} value="anime">
              Add Anime
            </option>
            <option className={style.opt} value="song">
              Add Song
            </option>
            <option className={style.opt} value="joke">
              Add Joke
            </option>
          </select>
        </form>
        {
          valueCategory && 
          <MenuFinder category={valueCategory}/>
        }

      </div>
    </>
  );
};

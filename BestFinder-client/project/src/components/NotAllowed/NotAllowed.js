import { MyNavBar } from "../Header/MyNavBar";
import style from "./NotAllowed.module.css";

export const NotAllowed = () => {
  return (
    <div className={style.container}>
      <MyNavBar />
      <h1 className={style.header}>Not allowed to make such operations!</h1>
    </div>
  );
};

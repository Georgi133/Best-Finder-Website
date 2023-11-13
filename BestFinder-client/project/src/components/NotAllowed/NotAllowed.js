import { MyNavBar } from "../Header/MyNavBar";
import style from "./NotAllowed.module.css";

export const NotAllowed = () => {
  return (
    <>
    <MyNavBar />
    <div className={style.container}>
      
      <h1 className={style.header}>Not allowed to make such operations!</h1>
      <img className={style.picture} src='https://e7.pngegg.com/pngimages/759/253/png-clipart-computer-icons-business-mobile-app-favicon-error-message-symbol-angle-logo.png' alt='errorPicture'/>
    </div>
    </>
  );
};

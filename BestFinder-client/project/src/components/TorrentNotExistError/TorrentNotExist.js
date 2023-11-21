import { useState } from "react";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./TorrentNotExist.module.css";
import { useNavigate } from "react-router-dom";

export const TorrentNotExist = () => {
  const navigate = useNavigate();
  const [reloadPage, setReloadPage] = useState(false);

  const reload = () => {
    setTimeout(redirect, 4000);
  };

  const redirect = () => {
    navigate("/");
    setReloadPage(false);
  };

  useState(() => {
    setReloadPage(true);
  }, []);

  return (
    <>
      <MyNavBar />
      <div className={style.container}>
        {reloadPage && reload()}
        <h1 className={style.header}>Torrent does not exist!</h1>
        <h3>We couldn't find the torrent that you are looking for.</h3>
        <h3>Please check the address and try again.</h3>
      </div>
    </>
  );
};

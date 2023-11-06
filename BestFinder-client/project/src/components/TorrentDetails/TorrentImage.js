import style from "./Details.module.css";

export const TorrentImage = ({
    pictureUrl,
}) => {
    
    return (
        <div className={style.imgContainer}>
            <img
              alt="anime"
              className={style.image}
              src={pictureUrl}
            />
          </div>
    )
}
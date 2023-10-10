import { Link } from "react-router-dom";
import style from "./MyNavBar.module.css";
import { useLocalStorage } from "../useLocalStorage/useLocalStorage";
import { useTranslation } from "react-i18next";

export const MyNavBar = ({ url }) => {
  const [lang, setLang] = useLocalStorage("lang", {});

  const { t, i18n } = useTranslation();

  const onChangeLanguage = (e) => {
    const lang = e.target.value;
    // setLanguage({ lang });
    setLang({ lang });
    i18n.changeLanguage(lang);
  };

  return (
    <div className={style.container}>
      <div className={style.innerContainer}>
        {url !== "home" && (
          <Link to="/" className={style.pg}>
            <strong>{t("home")}</strong>
          </Link>
        )}
        <p className={style.pg2}>
          <strong>{t("title")}</strong>
        </p>
      </div>

      <div className={style.innerContainer}>
        {url !== "login" && (
          <Link to="/login" className={style.pg}>
            <strong>{t("login")}</strong>
          </Link>
        )}
        {url !== "register" && (
          <Link to="/register" className={style.pg}>
            <strong>{t("register")}</strong>
          </Link>
        )}

        <select
          value={i18n.language}
          onChange={onChangeLanguage}
          className={style.selectContainer}
        >
            <option className={style.opt} value="bg">
              {t("language.bulgarian")}
            </option>
            <option className={style.opt} value="en">
              {t("language.english")}
            </option>
        </select>
      </div>
    </div>
  );
};

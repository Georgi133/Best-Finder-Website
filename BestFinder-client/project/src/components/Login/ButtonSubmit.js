import style from "./Login.module.css";
import { useTranslation } from "react-i18next";

export const ButtonSubmit = () => {

    const { t } = useTranslation();

    return (
        <button type="submit" className={style.login + " btn btn-primary"}>
          {t("userForm.submit")}
          </button>
    );
}
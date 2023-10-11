import { MyNavBar } from "../Header/MyNavBar";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Register.module.css";
import { useTranslation } from "react-i18next";
import { useForm } from "../useForm/useForm";
import { useAuthContext } from "../AuthContext/AuthContext";


export const Register = () => {

  const { t } = useTranslation()

  const { onRegisterSubmit } = useAuthContext();
  const {values , changeHandler , onSubmit} = useForm({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  }, onRegisterSubmit); 



  return (
    <>
      <MyNavBar url={"register"} />
      <div className={style.container}>
        <form onSubmit={onSubmit} className={style.formContainer}>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="username">
            {t("userForm.username")}:
            </label>
            <input
            value={values.username}
            onChange={changeHandler}
              type="text"
              name="username"
              className={style.inp + " form-control"}
              id="username"
              placeholder="Enter username"
              required
              min={3}
            />
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="email">
            {t("userForm.email")}:
            </label>
            <input
            value={values.email}
            onChange={changeHandler}
              type="email"
              name="email"
              className={style.inp + " form-control"}
              id="email"
              placeholder="Enter email"
              required
            />
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="password">
            {t("userForm.password")}:
            </label>
            <input
            value={values.password}
            onChange={changeHandler}
              type="password"
              className={style.inp + " form-control"}
              id="password"
              name="password"
              placeholder="Password"
              required
              min={3}
            />
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="confirmPassword">
            {t("userForm.confirmPassword")}:
            </label>
            <input
            value={values.confirmPassword}
            onChange={changeHandler}
              type="password"
              className={style.inp + " form-control"}
              id="confirmPassword"
              name="confirmPassword"
              placeholder="confirmPassword"
              required
              min={3}
            />
          </div>
          <div>
            <ButtonSubmit />
          </div>
        </form>
      </div>
    </>
  );
};

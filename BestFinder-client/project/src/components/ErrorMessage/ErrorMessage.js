import style from './Error.module.css'

export const ErrorMessage = ({
    message,
}) => {

    return (
        <div className={style.message}>{message}</div>
    )
}
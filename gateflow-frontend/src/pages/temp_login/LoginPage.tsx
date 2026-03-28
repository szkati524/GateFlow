import Header from "../../components/Common/Header";
import styles from './LoginPage.module.css';

const LoginPage = () => {
    return (
        <>
            <Header /> 
            <div className={styles.pageContainer}>
                <div className={styles.loginBox}>
                    <h2>Zaloguj się do GateFlow</h2>
                    <form onSubmit={(e) => e.preventDefault()}> 
                        <div className={styles.inputGroup}>
                            <label htmlFor="username">Nazwa użytkownika</label>
                            <input type="text" id="username" placeholder="login" />
                        </div>
                        <div className={styles.inputGroup}>
                            <label htmlFor="password">Hasło</label>
                            <input type="password" id="password" placeholder="hasło" />
                        </div>
                        <button type="submit" className={styles.loginButton}>
                            Zaloguj
                        </button>
                    </form>
                </div>
            </div>
        </>
    );
};

export default LoginPage;
import { useState } from "react";
import Header from "../../components/Common/Header";
import styles from './AddEntryPage.module.css';

const AddEntryPage = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    
  
    const [formData, setFormData] = useState({
        registration: '',
        brand: '',
        company: '',
        firstName: '',
        lastName: '',
        cargo: ''
    });

    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Dane do wysłania do Javy:", formData);
        alert("Pojazd dodany pomyślnie!");
     
    };

    return (
        <div className={styles.wrapper}>
            <Header />

           
            <div className={styles.topControls}>
                <button className={styles.menuTrigger} onClick={toggleSidebar}>
                    {isSidebarOpen ? '✕' : '☰'}
                </button>
                <div className={styles.rightActions}>
                    <button className={styles.iconBtn}>⚙️</button>
                    <button className={styles.logoutBtn}>Wyloguj się</button>
                </div>
            </div>

            {/* Sidebar */}
            <aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
                <nav className={styles.navMenu}>
                    <a href="/dashboard">Ruch pojazdów</a>
                    <a href="/add-entry">Dodaj wjazd</a>
                    <a href="#Search">Wyszukaj</a>
                    <a href="#Raport">Raporty</a>
                </nav>
            </aside>

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.formCard}>
                    <h2>Dodaj nowy wjazd</h2>
                    <form onSubmit={handleSubmit} className={styles.entryForm}>
                        <div className={styles.inputGroup}>
                            <label>Numer rejestracyjny</label>
                            <input type="text" name="registration" placeholder="np. KMY 12345" onChange={handleChange} required />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Marka pojazdu</label>
                            <input type="text" name="brand" placeholder="np. Scania" onChange={handleChange} />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Firma</label>
                            <input type="text" name="company" placeholder="Nazwa firmy" onChange={handleChange} />
                        </div>
                        <div className={styles.nameRow}>
                            <div className={styles.inputGroup}>
                                <label>Imię kierowcy</label>
                                <input type="text" name="firstName" placeholder="Imię" onChange={handleChange} />
                            </div>
                            <div className={styles.inputGroup}>
                                <label>Nazwisko kierowcy</label>
                                <input type="text" name="lastName" placeholder="Nazwisko" onChange={handleChange} />
                            </div>
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Ładunek</label>
                            <input type="text" name="cargo" placeholder="Co przewozi?" onChange={handleChange} />
                        </div>
                        <button type="submit" className={styles.submitBtn}>Zatwierdź wjazd</button>
                    </form>
                </div>
            </main>
        </div>
    );
};

export default AddEntryPage;
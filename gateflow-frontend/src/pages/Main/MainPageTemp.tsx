import { useState } from "react";
import Header from "../../components/Common/Header";
import styles from './MainPage.module.css';

interface VehicleEntry {
    id:number;
    registration: string;
    driverName: string;
    cargo:string;
    company:string;
    entryTime:string;
}
const initialEntries: VehicleEntry[] = [
{ id: 1, registration: 'KMY 12345', driverName: 'Jan Kowalski', cargo: 'Żwir', company: 'TRANS-POL', entryTime: '08:15' },
    { id: 2, registration: 'KRA 77889', driverName: 'Adam Nowak', cargo: 'Beton', company: 'CEMEX', entryTime: '09:00' },
];

const MainPage = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [entries,setEntries] = useState<VehicleEntry[]>(initialEntries);
    const [exits,setExits] = useState<VehicleEntry[]>([]);
    const [selectedEntryId,setSelectedEntryId] = useState<number | null>(null);
    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };
    const moveEntryToExit = () => {
        if(selectedEntryId == null) return;
        const entryToMove = entries.find(e => e.id === selectedEntryId);
        if(entryToMove) {
            setExits([...exits,entryToMove]);
            setEntries(entries.filter(e => e.id !== selectedEntryId));
            setSelectedEntryId(null);
        }
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

           
            <aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
                <nav className={styles.navMenu}>
                    <a href="#Main">Ruch pojazdów</a>
                    <a href="#Search">Wyszukaj</a>
                    <a href="#Raport">Raporty</a>
                </nav>
            </aside>

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.actionCenter}>
                    <button className={styles.addEntryBtn}>+ Dodaj Wjazd</button>
                </div>

                <div className={styles.listsContainer}>
                  
                    <section className={styles.listSection}>
                        <div className={styles.sectionHeader}>
                            <h2>Wjazdy pojazdów</h2>
                            <button className={styles.endStayBtn} onClick={moveEntryToExit} disabled={selectedEntryId === null}>
                                Zakończ pobyt
                            </button>
                        </div>
                        <table className={styles.vehicleTable}>
                            <thead>
                                <tr>
                                    <th>Edytuj</th>
                                    <th>Nr Rej</th>
                                    <th>Nazwisko</th>
                                    <th>Ładunek</th>
                                    <th>Firma</th>
                                    <th>Pobyt</th>
                                </tr>
                            </thead>
                            <tbody>
                                {entries.map(v => (
                                    <tr key={v.id} className={selectedEntryId === v.id ? styles.selectedRow : ''} onClick={() => setSelectedEntryId(v.id)}>
                                        <td><button className={styles.editBtn}>✏️</button></td>
                                        <td><strong>{v.registration}</strong></td>
                                        <td>{v.driverName}</td>
                                        <td>{v.cargo}</td>
                                        <td>{v.company}</td>
                                        <td>{v.entryTime}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </section>

                  
                    <section className={styles.listSection}>
                        <div className={styles.sectionHeader}><h2>Wyjazdy</h2></div>
                        <table className={styles.vehicleTable}>
                            <thead>
                                <tr>
                                    <th>Nr Rej</th>
                                    <th>Nazwisko</th>
                                    <th>Ładunek</th>
                                    <th>Firma</th>
                                    <th>Wyjazd</th>
                                </tr>
                            </thead>
                            <tbody>
                                {exits.map(v => (
                                    <tr key={v.id}>
                                        <td><strong>{v.registration}</strong></td>
                                        <td>{v.driverName}</td>
                                        <td>{v.cargo}</td>
                                        <td>{v.company}</td>
                                        <td>14:20</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </section>
                </div>
            </main>
        </div>
    );
};

export default MainPage;
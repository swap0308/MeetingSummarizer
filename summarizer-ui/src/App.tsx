import Chat from './components/Chat'
import Sidebar from './components/Sidebar'

function App() {

  return (
    <div className='h-[100vh] flex gap-[20px] p-[20px]'>
      <Chat />
      <Sidebar />
    </div>
  )
}

export default App

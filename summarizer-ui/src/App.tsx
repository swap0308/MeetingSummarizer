import Chat from './components/Chat'
import Sidebar from './components/Sidebar'
import { useEffect } from 'react'
import { baseUrl, serviceRequest } from './utils';

function App() {

  useEffect(() => {
    serviceRequest(`${baseUrl}/breeds`, {})
    ?.then(res => console.log(res, '//////////'))
    .catch(err => console.log(err, '//////////'));
  }, []);

  return (
    <div className='h-[100vh] flex gap-[20px] p-[20px]'>
      <Chat />
      <Sidebar />
    </div>
  )
}

export default App

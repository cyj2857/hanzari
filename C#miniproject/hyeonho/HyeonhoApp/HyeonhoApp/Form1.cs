using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Security;

namespace HyeonhoApp
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void 새로만들기ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (listView_schedule.Items.Count != 0)
            {
                if (MessageBox.Show("이미 작성된 내용이 존재합니다. 새로운 파일을 여시겠습니까?", "경고", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    Reset(); // dialog 초기화
                    listView_schedule.Items.Clear(); // 일정 초기화
                }
                else
                {
                    MessageBox.Show("취소했습니다.", "새로 만들기 취소");
                }
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            Reset(); // dialog 초기화
            label_nowTime.Text = "현재시각 " + DateTime.Now.ToString("HH:mm:ss"); // 현재시각 label 초기화
            monthCalendar1.MinDate = DateTime.Today; // 오늘 이전날짜 비활성화
        }

        private void Reset()
        {
            textBox_scheduleName.Text = null; // 일정제목 초기화
            textBox_scheduleContents.Text = null; // 일정내용 초기화
            foreach (string date in comboBox_startTime.Items)
            {
                if (DateTime.Compare(DateTime.Parse(date), DateTime.Now) > 0)
                {
                    comboBox_startTime.SelectedItem = date;
                    break;
                }

            } // 일정 시작시간 초기화
            comboBox_endTime.SelectedIndex = 1; // 일정 종료시간 초기화
            monthCalendar1.SetDate(DateTime.Today); // calendar 오늘 날짜로 이동
        }


        private void menuStrip1_ItemClicked(object sender, ToolStripItemClickedEventArgs e)
        {

        }

        private void splitContainer1_Panel1_Paint(object sender, PaintEventArgs e)
        {

        }

        private void monthCalendar1_DateChanged(object sender, DateRangeEventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            monthCalendar1.SetDate(DateTime.Today); // 오늘 날짜로 이동
        }

        private void comboBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox_endTime.Items.Clear();
            for (int i = comboBox_startTime.SelectedIndex; i < comboBox_startTime.Items.Count; i++)
            {
                comboBox_endTime.Items.Add(comboBox_startTime.Items[i]);
            }
            comboBox_endTime.SelectedIndex = 1;
        }

        private void label7_Click(object sender, EventArgs e)
        {

        }

        private void timer1_Tick_1(object sender, EventArgs e)
        {
            label_nowTime.Text = "현재시각 " + DateTime.Now.ToString("HH:mm:ss"); // 현재시각을 갱신해주는 timer. 1초마다 refresh
        }

        private void button_reset_Click(object sender, EventArgs e)
        {
            Reset(); // dialog 초기화
        }

        private void button_addSchedule_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrWhiteSpace(textBox_scheduleName.Text))
            {
                MessageBox.Show("일정을 입력하세요!");
            }

            else
            {
                ListViewItem newItem = new ListViewItem(monthCalendar1.SelectionRange.Start.ToShortDateString()); // 날짜
                newItem.SubItems.Add(comboBox_startTime.SelectedItem.ToString() + "~" + comboBox_endTime.SelectedItem.ToString()); // 시간
                newItem.SubItems.Add(textBox_scheduleName.Text); // 제목
                newItem.SubItems.Add(textBox_scheduleContents.Text); // 내용
                listView_schedule.Items.Add(newItem); // 새로운 일정 추가
                Reset(); // dialog 초기화
                MessageBox.Show("새 일정 등록 완료");
            }
        }

        private void label_scheduleName_Click(object sender, EventArgs e)
        {

        }

        private void comboBox_endTime_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void listView_schedule_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void button_deleteSchedule_Click(object sender, EventArgs e)
        {
            if (listView_schedule.SelectedItems.Count != 0)
            {
                if (MessageBox.Show("선택한 일정을 삭제하시겠습니까?", "경고", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    foreach (ListViewItem eachItem in listView_schedule.SelectedItems)
                    {
                        listView_schedule.Items.Remove(eachItem); // 선택된 모든 일정 삭제
                    }
                    MessageBox.Show("삭제했습니다.", "일정 삭제 완료");
                }
                else
                {
                    MessageBox.Show("취소했습니다.", "일정 삭제 취소");
                }
            }
            else
            {
                MessageBox.Show("삭제할 일정을 선택하세요.", "경고");
            }
        }

        private void button_scheduleModify_Click(object sender, EventArgs e)
        {
            if (listView_schedule.SelectedIndices.Count == 1)
            {
                Form2 newForm = new Form2(listView_schedule); // 일정을 수정하는 새로운 폼 생성
                newForm.Show();
            }
            else if (listView_schedule.SelectedIndices.Count > 1)
            {
                MessageBox.Show("한 개의 일정만 선택하세요."); // multiSelect 시 제한
            }
        }

        private void 저장SToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SaveFile(); // 파일로 저장
        }

        private void 열기OToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (listView_schedule.Items.Count != 0)
            {
                if (MessageBox.Show("이미 작성된 내용이 존재합니다. 새로운 파일을 여시겠습니까?", "경고", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    Reset(); // dialog 초기화
                    listView_schedule.Items.Clear(); // listView 초기화
                    OpenFile(); // 파일 오픈
                }
                else
                {
                    MessageBox.Show("취소했습니다.", "열기 취소");
                }
            }
            else
            {
                OpenFile();
            }
        }
        private void SaveFile()
        {
            SaveFileDialog sfd = new SaveFileDialog();
            sfd.Filter = "Text file (*.txt)|*.txt|C# file (*.cs)|*.cs"; // 확장자 필터 지정
            sfd.InitialDirectory = @"C:\Users\hancom\desktop"; // 기본 디렉토리 지정

            if (sfd.ShowDialog() == DialogResult.OK)
            {
                try
                {
                    StringBuilder fullString = new StringBuilder();
                    foreach (ListViewItem item in listView_schedule.Items)
                    {
                        StringBuilder sb = new StringBuilder(
                            item.SubItems[0].Text + "%" +
                            item.SubItems[1].Text + "%" +
                            item.SubItems[2].Text + "%" +
                            item.SubItems[3].Text + "\n");
                        fullString.Append(sb);
                    }


                    var sw = new StreamWriter(sfd.FileName);
                    sw.Write(fullString.ToString());
                    sw.Close();
                }
                catch (SecurityException ex)
                {
                    MessageBox.Show($"Security error.\n\nError message: {ex.Message}\n\n" +
                    $"Details:\n\n{ex.StackTrace}");
                }
            }
        }
        private void OpenFile()
        {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.Filter = "Text file (*.txt)|*.txt|C# file (*.cs)|*.cs"; // 확장자 필터 지정
            ofd.InitialDirectory = @"C:\Users\hancom\desktop"; // 기본 디렉토리 지정

            if (ofd.ShowDialog() == DialogResult.OK)
            {
                try
                {
                    var sr = new StreamReader(ofd.FileName);
                    String line;
                    while ((line = sr.ReadLine()) != null)
                    {
                        String[] inerLine = line.Split('%');
                        ListViewItem newItem = new ListViewItem(inerLine[0]);
                        newItem.SubItems.Add(inerLine[1]);
                        newItem.SubItems.Add(inerLine[2]);
                        newItem.SubItems.Add(inerLine[3]);
                        listView_schedule.Items.Add(newItem);
                    }
                    sr.Close();
                }
                catch (SecurityException ex)
                {
                    MessageBox.Show($"Security error.\n\nError message: {ex.Message}\n\n" +
                    $"Details:\n\n{ex.StackTrace}");
                }
            }
        }

        private void 도움말ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            
        }

        private void 피드백보내기ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            System.Diagnostics.Process.Start("https://github.com/AhnHyeonho");
        }

        private void 프로그램정보IToolStripMenuItem_Click(object sender, EventArgs e)
        {
            MessageBox.Show("Made By AhnHyeonho\nhttps://github.com/AhnHyeonho", "일정관리프로그램");
        }

        private void 도움말보기HToolStripMenuItem_Click(object sender, EventArgs e)
        {
            System.Diagnostics.Process.Start("https://github.com/hyori100/hancomFamily/blob/master/C%23miniproject/hyeonho/ProjectManual.md");
        }
    }
}

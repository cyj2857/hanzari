using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

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

        }

        private void Form1_Load(object sender, EventArgs e)
        {
            reset();
            label_nowTime.Text = "현재시각 " + DateTime.Now.ToString("HH:mm:ss"); // 현재시각 label 초기화
            monthCalendar1.MinDate = DateTime.Today;
        }

        private void reset()
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
            // 오늘 날짜로 이동
            monthCalendar1.SetDate(DateTime.Today);
        }

        private void comboBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox_endTime.Items.Clear();
            for (int i = comboBox_startTime.SelectedIndex ; i < comboBox_startTime.Items.Count; i++)
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
            label_nowTime.Text = "현재시각 " + DateTime.Now.ToString("HH:mm:ss");
        }

        private void button_reset_Click(object sender, EventArgs e)
        {
            reset();
        }

        private void button_addSchedule_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrWhiteSpace(textBox_scheduleName.Text))
            {
                MessageBox.Show("일정을 입력하세요!");
            }

            else
            {
                ListViewItem newItem = new ListViewItem(monthCalendar1.SelectionRange.Start.ToShortDateString());
                newItem.SubItems.Add(comboBox_startTime.SelectedItem.ToString() + "~" + comboBox_endTime.SelectedItem.ToString());
                newItem.SubItems.Add(textBox_scheduleName.Text);
                newItem.SubItems.Add(textBox_scheduleContents.Text);
                listView_schedule.Items.Add(newItem); // 일정 날짜
                reset();
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
            if(listView_schedule.SelectedItems.Count != 0)
            {
                if (MessageBox.Show("선택한 일정을 삭제하시겠습니까?", "경고", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    foreach (ListViewItem eachItem in listView_schedule.SelectedItems)
                    {
                        listView_schedule.Items.Remove(eachItem);
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
            if(listView_schedule.SelectedIndices.Count==1)
            {
                Form2 newForm = new Form2(listView_schedule);
                newForm.Show();
            }
            else if(listView_schedule.SelectedIndices.Count > 1)
            {
                MessageBox.Show("한 개의 일정만 선택하세요.");
            }
        }
    }
}

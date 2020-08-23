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

        }

        private void menuStrip1_ItemClicked(object sender, ToolStripItemClickedEventArgs e)
        {

        }

        private void splitContainer1_Panel1_Paint(object sender, PaintEventArgs e)
        {

        }

        private void textBox1_TextChanged_1(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            if(String.IsNullOrWhiteSpace(textBox1.Text))
            {
                MessageBox.Show("내용을 입력하세요!");
            }

            else
            {
                checkedListBox1.Items.Add(textBox1.Text); // checklist 에 추가
                textBox1.Text = null; // textBox 내용 지워주기
                MessageBox.Show("새 일정 등록 완료");
            }
        }

        private void checkedListBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            progressBar1.Value = (int)Math.Abs((float)(checkedListBox1.CheckedItems.Count) / (float)(checkedListBox1.Items.Count) * 100);
        }

        private void monthCalendar1_DateChanged(object sender, DateRangeEventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            // 오늘 날짜로 이동
            monthCalendar1.SetDate(DateTime.Today);
        }

        private void progressBar1_Click(object sender, EventArgs e)
        {

        }

    }
}
